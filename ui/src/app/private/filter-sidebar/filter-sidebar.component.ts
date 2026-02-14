import {Component, DestroyRef, inject, input, OnInit, output, signal} from '@angular/core';
import {combineLatest, debounceTime, distinctUntilChanged, map, Observable, switchMap, take} from 'rxjs';
import {
  FacetFilter,
  FacetPaginationRequestBody,
  GetFacetStatsResult,
  ValueCountPair
} from '@trackrejoice/typescriptmodels';
import {AsyncPipe, KeyValuePipe, TitleCasePipe} from '@angular/common';
import {toObservable} from '@angular/core/rxjs-interop';
import {HttpClient} from '@angular/common/http';
import {FormControl, FormGroup, FormRecord, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RouterLink} from '@angular/router';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatCheckbox} from '@angular/material/checkbox';

type FacetStatsMap = Record<string, ValueCountPair[]>;
type FacetValueGroup = FormRecord<FormControl<boolean>>;          // valueName -> boolean
type FacetsGroup = FormRecord<FacetValueGroup>;

@Component({
  selector: 'track-rejoice-filter-sidebar',
  imports: [
    AsyncPipe,
    KeyValuePipe,
    ReactiveFormsModule,
    RouterLink,
    MatFormField,
    MatLabel,
    MatInput,
    TitleCasePipe,
    FormsModule,
    MatCheckbox
  ],
  templateUrl: './filter-sidebar.component.html',
  styleUrl: './filter-sidebar.component.css',
})
export class FilterSidebarComponent implements OnInit {
  private http = inject(HttpClient);
  private destroyRef = inject(DestroyRef);
  getFacetStats: Observable<Map<string, ValueCountPair[]>>;
  facetFields = input.required<GetFacetStatsResult>();
  term = signal('');
  facet = signal<FacetFilter[]>([]);
  ready = signal(false);
  facetChange = output<[string, FacetFilter[]]>();
  filterChange$: Observable<[string, FacetFilter[]]>;
  form = new FormGroup({
    animal: new FormControl<string | null>(''),
  });
  searchForm = new FormGroup({
    search: new FormControl<string>(''),
    facets: new FormRecord<FacetsGroup>({}),

  });

  constructor() {

    // When term changes: reset + reload first page
    this.filterChange$ = combineLatest([toObservable(this.term).pipe(
      map(v => (v ?? '').trim()),
      debounceTime(300),
      distinctUntilChanged()), toObservable(this.facet)]
    );
  }


  ngOnInit(): void {
    const sub1 = this.searchForm.controls.search.valueChanges.subscribe(val => this.term.set(val))
    const sub2 = (this.searchForm.controls.facets as unknown as FacetsGroup).valueChanges.subscribe(facetsValue => {
      // facetsValue: Record<facetName, Record<valueName, boolean>>
      const filters: FacetFilter[] = Object.entries(facetsValue ?? {}).map(([facetName, valueMap]) => {
        const selectedValues = Object.entries(valueMap ?? {})
          .filter(([, checked]) => checked === true)
          .map(([valueName]) => valueName);

        return {facetName, values: selectedValues};
      }).filter(f => (f.values?.length ?? 0) > 0);

      this.facet.set(filters);
    });

    const sub3 = this.filterChange$.subscribe(([filter, facetFilters]) => this.facetChange.emit([filter, facetFilters]));

    this.getFacetStats = this.filterChange$.pipe(map(([filter, facetFilters]) => {
        return {
          facetFilters: facetFilters,
          filter: filter,
          pagination: {page: 0, pageSize: 10},
        } as FacetPaginationRequestBody;
      }),
      switchMap(body =>
        this.http.post<GetFacetStatsResult>('/api/sighting/list/stats', body, {withCredentials: true})),
      map(
        facetResults => {
          const stats: FacetStatsMap = facetResults.stats;
          const extraVals: FacetStatsMap = this.facetFields().stats;
          const response: Map<string, ValueCountPair[]> = new Map<string, ValueCountPair[]>();
          for (const [key, valueCountPair] of Object.entries(extraVals)) {
            response.set(key, []);
            for (const [responseKey, responseValueCountPair] of Object.entries(stats)) {
              if (key == responseKey) {
                for (const valueCount of valueCountPair.map(it => it.value)) {
                  const index = responseValueCountPair.findIndex(itr => itr.value == valueCount);
                  if (-1 < index) {
                    response.get(key).push(responseValueCountPair[index]);
                  } else {
                    response.get(key).push(<ValueCountPair>{count: 0, value: valueCount});
                  }
                }
              }
            }
          }
          return response;
        }
      ));

    const sub4 = this.getFacetStats.pipe(take(1))
      .subscribe(stats => {
        if (!this.ready()) {
          const facets = this.searchForm.controls.facets as unknown as FacetsGroup;
          Object.keys(facets.controls).forEach(facetName => facets.removeControl(facetName));

          for (const [facetName, values] of stats.entries()) {
            const valueGroup = new FormRecord<FormControl<boolean>>({});
            for (const v of values) {
              valueGroup.addControl(v.value, new FormControl<boolean>(false, {nonNullable: true}));
            }
            facets.addControl(facetName, valueGroup);
          }
          this.ready.set(true);
        }
      });

    this.destroyRef.onDestroy(() => {
      sub1.unsubscribe();
      sub2.unsubscribe();
      sub3.unsubscribe();
      sub4.unsubscribe();
    });
  }

  /*toggleOffIfSame(event: MouseEvent, index: number, value: string): void {
    if (this.form.controls.animal.value === value) {
      event.preventDefault();
      event.stopPropagation();
      this.form.controls.animal.setValue(null);
    }
  }*/

}
