import {ComponentFixture, TestBed} from '@angular/core/testing';
import {MapboxLocationPickerComponent} from './mapbox-location-picker.component';
import {By} from '@angular/platform-browser';

describe('MapboxLocationPickerComponent', () => {
  let component: MapboxLocationPickerComponent;
  let fixture: ComponentFixture<MapboxLocationPickerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MapboxLocationPickerComponent],
    });

    fixture = TestBed.createComponent(MapboxLocationPickerComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should show fallback when mapFailed is true', () => {
    component.mapFailed.set(true);
    fixture.detectChanges();

    const fallback = fixture.debugElement.query(By.css('.fallback-container'));
    expect(fallback).toBeTruthy();

    const message = fixture.debugElement.query(By.css('.fallback-message'));
    expect(message.nativeElement.textContent).toContain('Map could not be loaded');
  });

  it('should show fallback in test environment without Mapbox token', () => {
    // In test environment, mapboxToken is empty so initializeMap() sets mapFailed to true
    fixture.detectChanges();

    const fallback = fixture.debugElement.query(By.css('.fallback-container'));
    expect(fallback).toBeTruthy();
  });

  it('should emit locationSelected on fallback submit with valid coordinates', () => {
    component.mapFailed.set(true);
    fixture.detectChanges();

    const emitSpy = spyOn(component.locationSelected, 'emit');

    component.fallbackLng = 4.9876;
    component.fallbackLat = 52.1234;
    component.onFallbackSubmit();

    expect(emitSpy).toHaveBeenCalledWith({lng: 4.9876, lat: 52.1234});
  });

  it('should not emit locationSelected on fallback submit when coordinates are null', () => {
    component.mapFailed.set(true);
    fixture.detectChanges();

    const emitSpy = spyOn(component.locationSelected, 'emit');

    component.fallbackLng = null;
    component.fallbackLat = null;
    component.onFallbackSubmit();

    expect(emitSpy).not.toHaveBeenCalled();
  });

  it('should disable fallback button when coordinates are null', () => {
    component.mapFailed.set(true);
    fixture.detectChanges();

    const button = fixture.debugElement.query(By.css('.fallback-inputs button'));
    expect(button.nativeElement.disabled).toBeTrue();
  });

  it('should have locationSelected as an EventEmitter', () => {
    expect(component.locationSelected).toBeTruthy();
    expect(component.locationSelected.emit).toBeDefined();
  });

  it('should accept initialCenter input', () => {
    component.initialCenter = [10, 20];
    expect(component.initialCenter).toEqual([10, 20]);
  });
});
