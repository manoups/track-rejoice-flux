import {Component, inject, Input, input, OnInit, signal} from '@angular/core';
import {View} from '../../common/view';
import {FacetFilter, FacetPaginationRequestBody, Sighting, SightingDocument} from '@trackrejoice/typescriptmodels';
import {FormsModule} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {RouterLink} from '@angular/router';
import {CdkFixedSizeVirtualScroll, CdkVirtualForOf, CdkVirtualScrollViewport} from '@angular/cdk/scrolling';
import {DatePipe} from '@angular/common';
import {Observable} from 'rxjs';

@Component({
  selector: 'track-rejoice-sightings',
  imports: [
    FormsModule,
    RouterLink,
    CdkVirtualScrollViewport,
    CdkFixedSizeVirtualScroll,
    CdkVirtualForOf,
    DatePipe
  ],
  templateUrl: './sightings.component.html',
  styleUrl: './sightings.component.css',
})
export class SightingsComponent extends View implements OnInit{
  ngOnInit(): void {
      this.sightingsInput.subscribe(sightings => this.sightings.set(sightings));
  }
  private http = inject(HttpClient);
  @Input({required: true}) sightingsInput: Observable<SightingDocument[]>;

  // Sidebar inputs

  // Virtual/infinite state
  pageSize = 10;
  page = 0;
  loading = input.required<boolean>();
  localLoading = signal<boolean>(false);
  term=input.required<string>();
  private done = signal(false);

  sightings = signal<SightingDocument[]>([]);

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
    if (this.loading() || this.localLoading() || this.done()) return;

    // this.loading.set(true);

    // Backend-side paging/filter is assumed (fits your query model pattern)
    // const term = (this.term() ?? '').trim();
    const url = '/api/sighting/list';
    this.http.post<Sighting[]>(url, <FacetPaginationRequestBody>{
      facetFilters: <FacetFilter[]>[],
      filter: this.term(),
      pagination: {page: this.page, pageSize: this.pageSize}
    }, {withCredentials: true})
      .subscribe({
        next: (rows) => {
          const nextRows = rows ?? [];
          this.sightings.update(existing => existing.concat(nextRows));
          this.page += 1;
          if (nextRows.length < this.pageSize) {
            this.done.set(true);
          }
          this.localLoading.set(false);
        },
        error: () => {
          this.localLoading.set(false);
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
