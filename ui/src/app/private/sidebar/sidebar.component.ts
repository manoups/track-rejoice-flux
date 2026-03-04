import {Component, DestroyRef, inject, input, OnInit, output, signal} from '@angular/core';
import {combineLatest, debounceTime, distinctUntilChanged, map, Observable, Subject, switchMap, take} from 'rxjs';
import {
  FacetFilter,
  FacetPaginationRequestBody,
  GetFacetStatsResult,
  ValueCountPair
} from '@trackrejoice/typescriptmodels';
import {HttpClient} from '@angular/common/http';
import {toObservable} from '@angular/core/rxjs-interop';
import {FormControl, FormGroup, FormRecord, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AsyncPipe, KeyValuePipe, TitleCasePipe} from '@angular/common';
import {MatButton} from '@angular/material/button';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatListOption, MatSelectionList} from '@angular/material/list';
import {RouterLink} from '@angular/router';

type FacetStatsMap = Record<string, ValueCountPair[]>;

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
  filterChange = output<[string, FacetFilter[]]>();
  createOrUpdateFacets = new Subject<Map<string, ValueCountPair[]>>();
  getFacetStats: Observable<FacetStatsMap>;
  filterChange$: Observable<[string, FacetFilter[]]>;
  initBody: FacetPaginationRequestBody = {facetFilters: [], filter: "", pagination: {page: 0, pageSize: 10}}
  private filterMap: Map<string, ValueCountPair[]> = new Map();
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
    // 1. Initialize
    this.getFacetStats = this.http.post<GetFacetStatsResult>(this.statsEndpoint(), this.initBody, {withCredentials: true}).pipe(
      map(facetResults => facetResults.stats));
    this.getFacetStats.pipe(take(1))
      .subscribe(facetResults => {
        // const values: FacetStatsMap = facetResults ?? {};
        for (const [facetName, allowedPairs] of Object.entries(facetResults ?? {})) {
          this.filterMap.set(facetName, allowedPairs);
        }
        this.createOrUpdateFacets.next(this.filterMap);
      })

    // 2. On filter change, recalculate count

    const searchValueChange$ = this.searchForm.controls.search.valueChanges.pipe(
      map(v => (v ?? '').trim()),
      debounceTime(500),
      distinctUntilChanged());

    const termChange = searchValueChange$.subscribe(v => this.term.set(v));

    const updateFacetCount$ = searchValueChange$.pipe(switchMap(val => {
      let body = {
        facetFilters: this.facet(),
        filter: val,
        pagination: {page: 0, pageSize: 10}
      } as FacetPaginationRequestBody;
      return this.http.post<GetFacetStatsResult>('/api/sighting/list/stats', body, {withCredentials: true})
        .pipe(map(facetResults => facetResults.stats))
    }))
      .subscribe(facetResults => {
        const response = new Map<string, ValueCountPair[]>();

        for (const [facetName, allowedPairs] of this.filterMap) {
          for (const [fetchedFacetName, fetchedAllowedPairs] of Object.entries(facetResults ?? {})) {
            if (fetchedFacetName === facetName) {
              const merged = (allowedPairs ?? []).map(ap => {
                const found = fetchedAllowedPairs.find(sp => sp.value.toLowerCase() === ap.value.toLowerCase());
                return found ?? ({value: ap.value.toLowerCase(), count: 0} as ValueCountPair);
              });
              response.set(facetName, merged);
            }

          }
        }
        this.filterMap = response;
        this.createOrUpdateFacets.next(this.filterMap);
      });

    const facetFilterChange = this.searchForm.controls.facets.valueChanges.pipe(
      map((facetsValue) => {
        return Object.entries(facetsValue ?? {})
          .map(([facetName, selectedValues]) => ({
            facetName,
            values: (selectedValues ?? []).filter(v => typeof v === 'string' && v.length > 0),
          })).filter(f => f.values.length > 0) as FacetFilter[];
      }))
      .subscribe((facetFilters) => {
        this.facet.set(facetFilters);
      });

    const sub3 = this.filterChange$.subscribe(ff => {
      this.filterChange.emit(ff);
    });

    this.destroyRef.onDestroy(() => {
      updateFacetCount$.unsubscribe();
      termChange.unsubscribe();
      facetFilterChange.unsubscribe();
      sub3.unsubscribe();
    })
  }

  facetSelectionControl(facetName: string): FormControl<string[]> {
    const facets = this.searchForm.controls.facets;
    const existing = facets.controls[facetName];
    if (existing) {
      return existing;
    }
    const created = new FormControl<string[]>([], {nonNullable: true});
    facets.addControl(facetName, created, {emitEvent: false});
    return created;
  }

  clearValues = (facetName: string) => this.searchForm.controls.facets.controls[facetName].reset();
}
