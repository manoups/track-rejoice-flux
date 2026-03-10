import {Component, DestroyRef, inject, input, OnInit, output, signal} from '@angular/core';
import {combineLatest, debounceTime, distinctUntilChanged, map, Observable, ReplaySubject, switchMap} from 'rxjs';
import {
  FacetFilter,
  FacetPaginationRequestBody,
  GetFacetStatsResult,
  ValueCountPair
} from '@trackrejoice/typescriptmodels';
import {HttpClient} from '@angular/common/http';
import {takeUntilDestroyed, toObservable} from '@angular/core/rxjs-interop';
import {FormControl, FormGroup, FormRecord, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AsyncPipe, KeyValuePipe, TitleCasePipe} from '@angular/common';
import {MatButton} from '@angular/material/button';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatListOption, MatSelectionList} from '@angular/material/list';
import {RouterLink} from '@angular/router';


@Component({
  selector: 'track-rejoice-sidebar',
  imports: [
    AsyncPipe,
    FormsModule,
    KeyValuePipe,
    MatButton,
    MatFormField,
    MatInput,
    MatLabel,
    MatListOption,
    MatSelectionList,
    ReactiveFormsModule,
    RouterLink,
    TitleCasePipe
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css',
})
export class SidebarComponent implements OnInit {
  private http = inject(HttpClient);
  private destroyRef = inject(DestroyRef);
  statsEndpoint = input.required<string>();
  term = signal('');
  facet = signal<FacetFilter[]>([]);
  initValues = input.required<Map<string, ValueCountPair[]>>();
  filterChange = output<[string, FacetFilter[]]>();
  private initialFacetMap!: Map<string, ValueCountPair[]>;
  createOrUpdateFacets = new ReplaySubject<Map<string, ValueCountPair[]>>(1);
  filterChange$: Observable<[string, FacetFilter[]]>;
  searchForm = new FormGroup({
    search: new FormControl<string>(''),
    facets: new FormRecord<FormControl<string[]>>({}),

  });

  constructor() {

    // When term changes: reset + reload first page
    this.filterChange$ = combineLatest([toObservable(this.term).pipe(
      map(v => (v ?? '').trim()),
      debounceTime(500),
      distinctUntilChanged()), toObservable(this.facet)]
    );
  }

  ngOnInit(): void {
    this.initialFacetMap = this.initValues();

    this.syncFacetControls(this.initialFacetMap);
    this.createOrUpdateFacets.next(this.initialFacetMap);

    const searchValueChange$ = this.searchForm.controls.search.valueChanges.pipe(
      map(v => (v ?? '').trim()),
      debounceTime(500),
      distinctUntilChanged()
    );

    searchValueChange$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(value => this.term.set(value));

    searchValueChange$
      .pipe(
        switchMap(filter => {
          const body: FacetPaginationRequestBody = {
            facetFilters: this.facet(),
            filter,
            pagination: {page: 0, pageSize: 10}
          };

          return this.http.post<GetFacetStatsResult>(this.statsEndpoint(), body, {withCredentials: true}).pipe(
            map(result => result.stats),
            map(stats => this.mergeWithInitialValues(stats))
          );
        }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe(mergedFacetMap => {
        this.syncFacetControls(mergedFacetMap);
        this.createOrUpdateFacets.next(mergedFacetMap);
      });

    this.searchForm.controls.facets.valueChanges
      .pipe(
        map(facetsValue => Object.entries(facetsValue ?? {})
          .map(([facetName, selectedValues]) => ({
            facetName,
            values: (selectedValues ?? []).filter(v => typeof v === 'string' && v.length > 0),
          }))
          .filter(f => f.values.length > 0) as FacetFilter[]),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe(facetFilters => {
        this.facet.set(facetFilters);
      });

    this.filterChange$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(value => {
        this.filterChange.emit(value);
      });
  }

  private mergeWithInitialValues(
    stats: Record<string, ValueCountPair[]> | null | undefined
  ): Map<string, ValueCountPair[]> {
    const merged = new Map<string, ValueCountPair[]>();

    for (const [facetName, initialPairs] of this.initialFacetMap) {
      const fetchedPairs = stats?.[facetName] ?? [];

      merged.set(
        facetName,
        (initialPairs ?? []).map(initialPair => {
          const found = fetchedPairs.find(
            fetched => fetched.value.toLowerCase() === initialPair.value.toLowerCase()
          );

          return found ?? ({value: initialPair.value, count: 0} as ValueCountPair);
        })
      );
    }

    return merged;
  }

  private syncFacetControls(facetMap: Map<string, ValueCountPair[]>): void {
    const facets = this.searchForm.controls.facets;

    for (const facetName of Object.keys(facets.controls)) {
      if (!facetMap.has(facetName)) {
        facets.removeControl(facetName, {emitEvent: false});
      }
    }

    for (const [facetName, values] of facetMap) {
      const allowedValues = values.map(v => v.value);
      const existing = facets.controls[facetName];

      if (!existing) {
        facets.addControl(
          facetName,
          new FormControl<string[]>(allowedValues, {nonNullable: true}),
          {emitEvent: false}
        );
        continue;
      }

      const currentValue = existing.getRawValue() ?? [];
      const nextValue = currentValue.filter(value => allowedValues.includes(value));

      if (
        currentValue.length !== nextValue.length ||
        currentValue.some((value, index) => value !== nextValue[index])
      ) {
        existing.setValue(nextValue, {emitEvent: false});
      }
    }
  }

  resetValues = (facetName: string) => {
    const control = this.searchForm.controls.facets.controls[facetName];
    const initialValues = this.initialFacetMap.get(facetName)?.map(v => v.value) ?? [];
    control?.setValue(initialValues);
  };
}
