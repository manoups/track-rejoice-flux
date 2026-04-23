import {
  AfterViewInit,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnDestroy,
  Output,
  signal,
  ViewChild,
} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatButton} from '@angular/material/button';
import mapboxgl from 'mapbox-gl';
import {environment} from '../../../../environments/environments';

@Component({
  selector: 'track-rejoice-mapbox-location-picker',
  imports: [FormsModule, MatFormField, MatLabel, MatInput, MatButton],
  templateUrl: './mapbox-location-picker.component.html',
  styleUrl: './mapbox-location-picker.component.css',
})
export class MapboxLocationPickerComponent implements AfterViewInit, OnDestroy {
  @Input() initialCenter?: [number, number];
  @Output() locationSelected = new EventEmitter<{ lng: number; lat: number }>();

  @ViewChild('mapContainer') mapContainer: ElementRef<HTMLDivElement>;

  mapFailed = signal(false);

  fallbackLng: number | null = null;
  fallbackLat: number | null = null;

  private map: mapboxgl.Map | null = null;
  private marker: mapboxgl.Marker | null = null;

  ngAfterViewInit(): void {
    this.initializeMap();
  }

  ngOnDestroy(): void {
    this.map?.remove();
  }

  private initializeMap(): void {
    try {
      const token = (environment as any).mapboxToken;
      if (!token) {
        this.mapFailed.set(true);
        return;
      }

      const center: [number, number] = this.initialCenter ?? [4.9, 52.37];

      this.map = new mapboxgl.Map({
        container: this.mapContainer.nativeElement,
        accessToken: token,
        style: 'mapbox://styles/mapbox/streets-v12',
        center,
        zoom: 10,
      });

      this.map.on('error', () => {
        this.mapFailed.set(true);
        this.map?.remove();
        this.map = null;
      });

      this.map.on('click', (e: mapboxgl.MapMouseEvent) => {
        const {lng, lat} = e.lngLat;
        this.placeMarker(lng, lat);
        this.locationSelected.emit({lng, lat});
      });
    } catch {
      this.mapFailed.set(true);
    }
  }

  private placeMarker(lng: number, lat: number): void {
    if (this.marker) {
      this.marker.setLngLat([lng, lat]);
    } else {
      this.marker = new mapboxgl.Marker()
        .setLngLat([lng, lat])
        .addTo(this.map!);
    }
  }

  onFallbackSubmit(): void {
    if (this.fallbackLng != null && this.fallbackLat != null) {
      this.locationSelected.emit({lng: this.fallbackLng, lat: this.fallbackLat});
    }
  }
}
