import {Component, inject, signal} from '@angular/core';
import {BehaviorSubject, debounceTime, distinctUntilChanged, finalize, forkJoin, map, Subject} from 'rxjs';
import {FacetPaginationRequestBody, GetFacetStatsResult, Sighting} from '@trackrejoice/typescriptmodels';
import {HttpClient} from '@angular/common/http';
import {FilterSidebarComponent} from '../filter-sidebar/filter-sidebar.component';
import {FormsModule} from '@angular/forms';
import {RouterLink} from '@angular/router';
import {SightingsComponent} from '../sightings/sightings.component';
import {toObservable, toSignal} from '@angular/core/rxjs-interop';
import {tap} from 'rxjs/operators';

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
export class SightingBoilerplateComponent {


  private http = inject(HttpClient);
  // filter = signal<string>('');
  page = 0;
  term = signal('');
  pageSize = 10;
  loading = signal(false);
  private done = signal(false);
  facetQuery$ = new Subject<GetFacetStatsResult>();
  dataQuery$ = new BehaviorSubject<Sighting[]>([]);
  sightings=toSignal(this.dataQuery$);

  constructor() {

    // When term changes: reset + reload first page
    toObservable(this.term).pipe(
      tap(v => console.log('term changed: ' + v)),
      map(v => (v ?? '').trim()),
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(() => {
      this.resetAndLoad();
    });


    // initial load
    this.resetAndLoad();
  }

  private resetAndLoad(): void {
    this.page = 0;
    this.done.set(false);
    this.loadNextPage();
  }

  private loadNextPage(): void {
    if (this.loading() || this.done()) return;

    this.loading.set(true);

    // Backend-side paging/filter is assumed (fits your query model pattern)
    // const term = (this.term() ?? '').trim();
    const body = {
      filter: this.term(),
      pagination: { page: this.page, pageSize: this.pageSize },
    } as FacetPaginationRequestBody;

    forkJoin({
      stats: this.http.post<GetFacetStatsResult>('/api/sighting/list/stats', body, { withCredentials: true }),
      rows: this.http.post<Sighting[]>('/api/sighting/list', body, { withCredentials: true }),
    }).pipe(
      finalize(() => this.loading.set(false))
    ).subscribe({
      next: ({ stats, rows }) => {
        this.facetQuery$.next(stats);
        this.dataQuery$.next(rows);

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
}
