import {Component, inject, signal} from '@angular/core';
import {debounceTime, distinctUntilChanged, map} from 'rxjs';
import {View} from '../../common/view';
import {Sighting} from '@trackrejoice/typescriptmodels';
import {toObservable} from '@angular/core/rxjs-interop';
import {FormsModule} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {RouterLink} from '@angular/router';
import {CdkFixedSizeVirtualScroll, CdkVirtualForOf, CdkVirtualScrollViewport} from '@angular/cdk/scrolling';

@Component({
  selector: 'track-rejoice-sightings',
  imports: [
    FormsModule,
    RouterLink,
    CdkVirtualScrollViewport,
    CdkFixedSizeVirtualScroll,
    CdkVirtualForOf
  ],
  templateUrl: './sightings.component.html',
  styleUrl: './sightings.component.css',
})
export class SightingsComponent extends View {
  private http = inject(HttpClient);

  // Sidebar inputs
  term = signal('');

  // Virtual/infinite state
  pageSize = 25;
  private page = 0;
  private loading = signal(false);
  private done = signal(false);

  sightings = signal<Sighting[]>([]);

  constructor() {
    super();

    // When term changes: reset + reload first page
    toObservable(this.term).pipe(
      map(v => (v ?? '').trim()),
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(() => {
      this.resetAndLoad();
    });

    // initial load
    this.resetAndLoad();
  }

  onScrollIndexChange(index: number): void {
    // Load next page when user gets close to the end
    const buffer = 8;
    if (this.loading() || this.done()) return;
    if (index + buffer >= this.sightings().length) {
      this.loadNextPage();
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
      .subscribe({
        next: () => this.resetAndLoad()
      });
  }

  private resetAndLoad(): void {
    this.page = 0;
    this.done.set(false);
    this.sightings.set([]);
    this.loadNextPage();
  }

  private loadNextPage(): void {
    if (this.loading() || this.done()) return;

    this.loading.set(true);

    // Backend-side paging/filter is assumed (fits your query model pattern)
    const term = (this.term() ?? '').trim();
    const url =
      `/api/sighting?page=${this.page}&pageSize=${this.pageSize}` +
      (term ? `&filter=${encodeURIComponent(term)}` : '');

    this.http.get<Sighting[]>(url, {withCredentials: true})
      .subscribe({
        next: (rows) => {
          const nextRows = rows ?? [];
          this.sightings.update(existing => existing.concat(nextRows));
          this.page += 1;
          if (nextRows.length < this.pageSize) {
            this.done.set(true);
          }
          this.loading.set(false);
        },
        error: () => {
          this.loading.set(false);
        }
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
}
