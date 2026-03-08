import {inject, Injectable, signal} from '@angular/core';
import {TrackRejoiceDataSource} from '../common/datasource';
import {FacetPaginationRequestBody, Sighting, SightingDocument} from '@trackrejoice/typescriptmodels';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, finalize, Observable, of, Subject} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SightingListingService {
  dataSource = new TrackRejoiceDataSource<SightingDocument>();
  private http = inject(HttpClient);
  loading = signal<boolean>(false);
  done = signal<boolean>(false);
  page = 0;
  sightings = signal<SightingDocument[]>([]);
  clickedRow = signal<SightingDocument>(null);
  pageSubject = new BehaviorSubject<number>(0);
  private pageSize = 20;
  loadNextPage$ = new Subject<FacetPaginationRequestBody>();

  resetListing(): void {
    this.page = 0;
    this.sightings.set([]);
    this.dataSource.set([]);
    this.done.set(false);
    this.clickedRow.set(null);
  }

  loadNextPage(body: FacetPaginationRequestBody): Observable<Sighting[]> {
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
    if (this.loading() || this.done()) return;
    if (index + this.pageSize >= this.sightings().length) {
      this.pageSubject.next(this.page);
    }
  }

  deleteSighting(sighting: Sighting): void {
    const id = this.idToString((sighting as any).sightingId);
    if (!id) {
      alert('Cannot delete: missing sightingId');
      return;
    }
    if (!confirm(`Delete sighting ${id}?`)) return;

    /*this.http.delete(`/api/sighting/${encodeURIComponent(id)}`, {withCredentials: true})
      .pipe(tap(_ => this.sightingListingService.resetListing()))
      .subscribe({
        next: () => this.pageSubject.next(0)
      });*/
  }

  private idToString(id: any): string {
    if (id == null) return '';
    if (typeof id === 'string' || typeof id === 'number') return String(id);
    if (typeof id === 'object') return String(id.value ?? id.id ?? id.identifier ?? '');
    return String(id);
  }

}
