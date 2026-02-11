import {Component, DestroyRef, inject, OnInit, signal} from '@angular/core';
import {
  BehaviorSubject,
  combineLatest,
  debounceTime,
  distinctUntilChanged,
  finalize,
  forkJoin,
  map, Observable,
  Subject
} from 'rxjs';
import {FacetFilter, FacetPaginationRequestBody, GetFacetStatsResult, Sighting} from '@trackrejoice/typescriptmodels';
import {HttpClient} from '@angular/common/http';
import {FilterSidebarComponent} from '../filter-sidebar/filter-sidebar.component';
import {FormsModule} from '@angular/forms';
import {RouterLink} from '@angular/router';
import {SightingsComponent} from '../sightings/sightings.component';
import {toObservable} from '@angular/core/rxjs-interop';
import {LoadingService} from '../../common/loading.service';

@Component({
  selector: 'track-rejoice-sighting-boilerplate',
  imports: [
    FilterSidebarComponent,
    FormsModule,
    RouterLink,
    SightingsComponent
  ],
  templateUrl: './sighting-boilerplate.component.html',
  styleUrl: './sighting-boilerplate.component.css',
})
export class SightingBoilerplateComponent implements OnInit {
  private loadingService = inject(LoadingService);
  private http = inject(HttpClient);
  private destroyRef = inject(DestroyRef);
  page = 0;
  term = signal('');
  facet = signal<FacetFilter[]>([]);
  pageSize = 10;
  loading = signal(false);
  done = signal(false);
  facetQuery$ = new Subject<GetFacetStatsResult>();
  dataQuery$ = new BehaviorSubject<Sighting[]>([]);
  resetAndFeed$ = new BehaviorSubject<Sighting[]>([]);
  private filterChange$: Observable<[string, FacetFilter[]]>;

  // sightings = toSignal(this.dataQuery$);

  constructor() {

    // When term changes: reset + reload first page
    this.filterChange$ = combineLatest([toObservable(this.term).pipe(
      // tap(v => console.log('term changed: ' + v)),
      map(v => (v ?? '').trim()),
      debounceTime(300),
      distinctUntilChanged()), toObservable(this.facet)]
    );
  }

  ngOnInit(): void {
    const subscription = this.filterChange$.subscribe(_ => this.resetAndLoad());
    // initial load
    this.resetAndLoad();

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }

  resetAndLoad(): void {
    this.page = 0;
    this.done.set(false);
    this.loadNextPage(true);
  }

  private loadNextPage(reset: boolean): void {
    if (this.loading() || this.done()) return;

    this.loading.set(true);

    // Backend-side paging/filter is assumed (fits your query model pattern)
    const term = (this.term() ?? '').trim();
    const body = {
      facetFilters: this.facet(),
      filter: term,
      pagination: {page: this.page, pageSize: this.pageSize},
    } as FacetPaginationRequestBody;

    forkJoin({
      stats: this.http.post<GetFacetStatsResult>('/api/sighting/list/stats', body, {withCredentials: true}),
      rows: this.http.post<Sighting[]>('/api/sighting/list', body, {withCredentials: true}),
    }).pipe(
      finalize(() => {
        this.loading.set(false);
      })
    ).subscribe({
      next: ({stats, rows}) => {
        this.facetQuery$.next(stats);
        if (reset) {
          this.resetAndFeed$.next(rows);
        } else {
          this.dataQuery$.next(rows);
        }

        this.page += 1;
        if ((rows ?? []).length < this.pageSize) {
          this.done.set(true);
        }
      },
      error: () => {
        // optional: show error state here; loading already reset by finalize()
      }
    });
  }

  onFilterChanges(event$: string): void {
    this.term.set(event$);
  }

  onFacetsChanged(event$: any): void {

  }

  protected onPageChange(event: any): void {
    this.loadNextPage(false)
  }
}
