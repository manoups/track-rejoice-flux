import {Component, inject, Input, input, OnInit, output, signal} from '@angular/core';
import {View} from '../../common/view';
import {Sighting, SightingDocument} from '@trackrejoice/typescriptmodels';
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
export class SightingsComponent extends View implements OnInit {
  ngOnInit(): void {
    this.resetAndFeed$.subscribe(sightings => {
      this.sightings.set(sightings)
    });
    this.nextPage$.subscribe(sightings => this.sightings.update(existing => existing.concat(sightings)));
  }

  private http = inject(HttpClient);
  @Input({required: true}) nextPage$: Observable<SightingDocument[]>;
  @Input({required: true}) resetAndFeed$: Observable<SightingDocument[]>;
  pageChange = output<any>();
  resetAndLoad = output<boolean>();
  // Sidebar inputs

  // Virtual/infinite state
  loading = input.required<boolean>();
  term = input.required<string>();
  done = input.required<boolean>();

  sightings = signal<SightingDocument[]>([]);

  onScrollIndexChange(index: number): void {
    // Load next page when user gets close to the end
    const buffer = 8;
    if (this.loading() || this.done()) return;
    if (index + buffer >= this.sightings().length) {
      this.pageChange.emit('');
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
        next: () => this.resetAndLoad.emit(true)
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
