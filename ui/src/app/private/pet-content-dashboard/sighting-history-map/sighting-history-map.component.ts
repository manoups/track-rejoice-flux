import {Component, DestroyRef, ElementRef, inject, input, OnInit, signal, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {KeyValuePair, SightingDetails} from '@trackrejoice/typescriptmodels';
import mapboxgl from 'mapbox-gl';

@Component({
  selector: 'track-rejoice-sighting-history-map',
  imports: [],
  templateUrl: './sighting-history-map.component.html',
  styleUrl: './sighting-history-map.component.css',
})
export class SightingHistoryMapComponent implements OnInit {
  private http = inject(HttpClient);
  private destroyRef = inject(DestroyRef);

  contentId = input.required<string>();
  lastConfirmedSighting = input<SightingDetails | null>(null);

  noLocationData = signal(false);
  loading = signal(true);

  @ViewChild('mapContainer', {static: true}) mapContainer!: ElementRef<HTMLDivElement>;

  private map?: mapboxgl.Map;

  ngOnInit(): void {
    this.http.get<KeyValuePair[]>(
      `/api/content/${encodeURIComponent(this.contentId())}/sighting-history`,
      {withCredentials: true}
    ).subscribe({
      next: (history) => this.initMap(history),
      error: () => {
        this.loading.set(false);
        this.noLocationData.set(true);
      }
    });
  }

  private initMap(history: KeyValuePair[]): void {
    this.loading.set(false);

    // Sort by timestamp (key) chronologically
    const sorted = [...history].sort((a, b) => {
      const timeA = new Date(a.key).getTime();
      const timeB = new Date(b.key).getTime();
      return timeA - timeB;
    });

    const coordinates: { lng: number; lat: number; timestamp: string }[] = sorted
      .filter(entry => entry.value?.lng != null && entry.value?.lat != null)
      .map(entry => ({
        lng: Number(entry.value.lng),
        lat: Number(entry.value.lat),
        timestamp: String(entry.key),
      }));

    if (coordinates.length === 0) {
      const last = this.lastConfirmedSighting();
      if (last?.lng != null && last?.lat != null) {
        this.createMap(Number(last.lng), Number(last.lat), 12);
        return;
      }
      this.noLocationData.set(true);
      return;
    }

    // Create map centered on first coordinate
    this.createMap(coordinates[0].lng, coordinates[0].lat, 12);

    this.map!.on('load', () => {
      this.addMarkersAndPolyline(coordinates);
      this.fitBoundsToMarkers(coordinates);
    });
  }

  private createMap(lng: number, lat: number, zoom: number): void {
    (mapboxgl as any).accessToken = 'pk.eyJ1IjoiYnJlZWNlIiwiYSI6ImNtYnRhcjRhNjBhNjcya3B3ZGRtMGRqNjkifQ.GJfMBpHxSMHOPGG-GcGMjQ';

    this.map = new mapboxgl.Map({
      container: this.mapContainer.nativeElement,
      style: 'mapbox://styles/mapbox/streets-v12',
      center: [lng, lat],
      zoom,
    });

    this.map.addControl(new mapboxgl.NavigationControl(), 'top-right');

    this.destroyRef.onDestroy(() => {
      this.map?.remove();
    });
  }

  private addMarkersAndPolyline(coordinates: { lng: number; lat: number; timestamp: string }[]): void {
    if (!this.map) return;

    // Add markers with popups
    for (const coord of coordinates) {
      const popup = new mapboxgl.Popup({offset: 25}).setText(coord.timestamp);

      new mapboxgl.Marker()
        .setLngLat([coord.lng, coord.lat])
        .setPopup(popup)
        .addTo(this.map);
    }

    // Add polyline connecting markers chronologically
    if (coordinates.length > 1) {
      this.map.addSource('sighting-route', {
        type: 'geojson',
        data: {
          type: 'Feature',
          properties: {},
          geometry: {
            type: 'LineString',
            coordinates: coordinates.map(c => [c.lng, c.lat]),
          },
        },
      });

      this.map.addLayer({
        id: 'sighting-route-line',
        type: 'line',
        source: 'sighting-route',
        layout: {
          'line-join': 'round',
          'line-cap': 'round',
        },
        paint: {
          'line-color': '#3b82f6',
          'line-width': 3,
        },
      });
    }
  }

  private fitBoundsToMarkers(coordinates: { lng: number; lat: number }[]): void {
    if (!this.map || coordinates.length === 0) return;

    const bounds = new mapboxgl.LngLatBounds();
    for (const coord of coordinates) {
      bounds.extend([coord.lng, coord.lat]);
    }

    this.map.fitBounds(bounds, {padding: 50});
  }
}
