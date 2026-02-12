import {Component, DestroyRef, inject, Input, OnInit, signal} from '@angular/core';
import {View} from '../../common/view';
import {FacetFilter, FacetPaginationRequestBody, Sighting, SightingDocument} from '@trackrejoice/typescriptmodels';
import {FormsModule} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {CdkFixedSizeVirtualScroll, CdkVirtualScrollViewport} from '@angular/cdk/scrolling';
import {DatePipe} from '@angular/common';
import {BehaviorSubject, finalize, map, merge, Observable, of, switchMap, withLatestFrom} from 'rxjs';
import {tap} from 'rxjs/operators';
import {DataSource} from '@angular/cdk/collections';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable
} from '@angular/material/table';

class SightingsDataSource extends DataSource<SightingDocument> {
  private readonly subject = new BehaviorSubject<SightingDocument[]>([]);

  connect(): Observable<SightingDocument[]> {
    return this.subject.asObservable();
  }

  disconnect(): void {
    this.subject.complete();
  }

  set(rows: SightingDocument[]): void {
    this.subject.next(rows);
  }

  append(rows: SightingDocument[]): void {
    const current = this.subject.value;
    this.subject.next(current.concat(rows ?? []));
  }

  get length(): number {
    return this.subject.value.length;
  }
}

@Component({
  selector: 'track-rejoice-sightings',
  imports: [
    FormsModule,
    CdkVirtualScrollViewport,
    CdkFixedSizeVirtualScroll,
    DatePipe,
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatHeaderCellDef,
    MatCellDef,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRowDef,
    MatRow
  ],
  templateUrl: './sightings.component.html',
  styleUrl: './sightings.component.css',
  standalone: true
})
export class SightingsComponent extends View implements OnInit {
  private http = inject(HttpClient);
  private destroyRef = inject(DestroyRef);
  // Virtual/infinite state
  loading = signal<boolean>(false);
  done = signal<boolean>(false);
  @Input({required: true}) filterUpdate$: Observable<[string, FacetFilter[]]>;
  private pageSize = 10;
  page = 0;
  rowHeight = 48;
  pageSubject = new BehaviorSubject<number>(0);

  sightings = signal<SightingDocument[]>([]);
  dataSource = new SightingsDataSource();
  constructor() {
    super();
    // this.facetUpdate$()
  }

  ngOnInit(): void {
    const filterObservable$: Observable<FacetPaginationRequestBody> = this.filterUpdate$
      .pipe(tap(_ => this.resetListing()),
        map(([filter, facetFilters]) => {
          return {filter: filter, facetFilters: facetFilters, pagination: {page: 0, pageSize: this.pageSize}}
        }));

    const pageObservable$: Observable<FacetPaginationRequestBody> = this.pageSubject.pipe(
      withLatestFrom(this.filterUpdate$),
      map(([page, [filter, facetFilters]]) => {
        return {filter: filter, facetFilters: facetFilters, pagination: {page: page, pageSize: this.pageSize}}
      }))

    const subscribe = merge(filterObservable$, pageObservable$).pipe(switchMap((body) => this.loadNextPage(body)))
      .subscribe({
        next: (rows) => {
          this.sightings.update(sightings => sightings.concat(rows))
          this.dataSource.append(rows);
          this.page += 1;
          if ((rows).length < this.pageSize) {
            this.done.set(true);
          }
        },
        error: () => {
          // optional: show error state here; loading already reset by finalize()
        }
      });

    this.destroyRef.onDestroy(() => {
      subscribe.unsubscribe();
    });
  }

  resetListing(): void {
    this.page = 0;
    this.sightings.set([]);
    this.dataSource.set([]);
    this.done.set(false);
  }

  private loadNextPage(body: FacetPaginationRequestBody): Observable<Sighting[]> {
    if (this.loading() || this.done()) return of([]);

    this.loading.set(true);

    return this.http.post<Sighting[]>('/api/sighting/list', body, {withCredentials: true}).pipe(
      finalize(() => {
        this.loading.set(false);
      })
    )
  }

  onScrollIndexChange(index: number): void {
    // Load next page when user gets close to the end
    const buffer = 8;
    if (this.loading() || this.done()) return;
    if (index + buffer >= this.sightings().length) {
      this.pageSubject.next(this.page + 1);
    }
  }

  deleteSighting(sighting: Sighting): void {
    const id = this.idToString((sighting as any).sightingId);
    if (!id) {
      alert('Cannot delete: missing sightingId');
      return;
    }
    if (!confirm(`Delete sighting ${id}?`)) return;

    this.http.delete(`/api/sighting/${encodeURIComponent(id)}`, {withCredentials: true})
      .pipe(tap(_ => this.resetListing()))
      .subscribe({
        next: () => this.pageSubject.next(0)
      });
  }

  private idToString(id: any): string {
    if (id == null) return '';
    if (typeof id === 'string' || typeof id === 'number') return String(id);
    if (typeof id === 'object') return String(id.value ?? id.id ?? id.identifier ?? '');
    return String(id);
  }

  trackBySightingId = (_index: number, sighting: Sighting): any => {
    return (sighting as any).sightingId;
  };
  protected displayedColumns=['id', 'ownerId', 'timestamp'];
}
