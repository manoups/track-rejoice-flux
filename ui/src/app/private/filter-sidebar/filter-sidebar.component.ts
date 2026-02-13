import {Component, DestroyRef, inject, input, OnInit, output, signal} from '@angular/core';
import {combineLatest, debounceTime, distinctUntilChanged, filter, map, Observable, switchMap} from 'rxjs';
import {
  FacetFilter,
  FacetPaginationRequestBody,
  GetFacetStatsResult,
  ValueCountPair
} from '@trackrejoice/typescriptmodels';
import {AsyncPipe, KeyValuePipe, TitleCasePipe} from '@angular/common';
import {toObservable} from '@angular/core/rxjs-interop';
import {HttpClient} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {RouterLink} from '@angular/router';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {forEach} from 'lodash';
import {MatRadioButton, MatRadioGroup} from '@angular/material/radio';

type FacetStatsMap = Record<string, ValueCountPair[]>;


@Component({
  selector: 'track-rejoice-filter-sidebar',
  imports: [
    AsyncPipe,
    KeyValuePipe,
    FormsModule,
    RouterLink,
    MatFormField,
    MatLabel,
    MatInput,
    MatRadioGroup,
    TitleCasePipe,
    MatRadioButton
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
  facetChange = output<[string, FacetFilter[]]>();
  filterChange$: Observable<[string, FacetFilter[]]>;

  constructor() {

    // When term changes: reset + reload first page
    this.filterChange$ = combineLatest([toObservable(this.term).pipe(
      map(v => (v ?? '').trim()),
      debounceTime(300),
      distinctUntilChanged()), toObservable(this.facet)]
    );
  }


  ngOnInit(): void {
    const subscribe = this.filterChange$.subscribe(([filter, facetFilters]) => this.facetChange.emit([filter, facetFilters]));

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

    this.destroyRef.onDestroy(() => {
      subscribe.unsubscribe();
    });
  }
}
