import * as fc from 'fast-check';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {Component, EventEmitter, Output} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {of} from 'rxjs';
import {Router} from '@angular/router';
import {PetDetailsFormComponent} from './pet-details-form.component';
import {MapboxLocationPickerComponent} from '../mapbox-location-picker/mapbox-location-picker.component';
import {QueryGateway} from '../../../common/query-gateway';
import {CommandGateway} from '../../../common/command-gateway';
import {HandlerRegistry} from '../../../common/handler-registry';

@Component({
  selector: 'track-rejoice-mapbox-location-picker',
  template: '',
})
class MockMapboxLocationPickerComponent {
  @Output() locationSelected = new EventEmitter<{ lng: number; lat: number }>();
}

describe('PetDetailsFormComponent', () => {

  // Feature: pet-content-creation-journey, Property 1: Required field validation determines form submittability
  // Validates: Requirements 1.2, 2.4, 3.1, 3.2, 3.3
  describe('Property 1: Required field validation determines form submittability', () => {
    let fixture: ComponentFixture<PetDetailsFormComponent>;
    let component: PetDetailsFormComponent;

    beforeEach(() => {
      const httpSpy = jasmine.createSpyObj('HttpClient', ['get', 'post']);

      TestBed.configureTestingModule({
        imports: [PetDetailsFormComponent],
        providers: [
          {provide: HttpClient, useValue: httpSpy},
          QueryGateway,
          CommandGateway,
          HandlerRegistry,
        ],
      }).overrideComponent(PetDetailsFormComponent, {
        remove: {imports: [MapboxLocationPickerComponent]},
        add: {imports: [MockMapboxLocationPickerComponent]},
      });

      fixture = TestBed.createComponent(PetDetailsFormComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    afterEach(() => {
      fixture.destroy();
    });

    const requiredFields = ['name', 'breed', 'gender', 'subtype', 'lng', 'lat'] as const;

    // Arbitrary that generates a random subset of required fields to be present (true) or null (false)
    const requiredFieldPresenceArbitrary = fc.record({
      name: fc.boolean(),
      breed: fc.boolean(),
      gender: fc.boolean(),
      subtype: fc.boolean(),
      lng: fc.boolean(),
      lat: fc.boolean(),
    });

    // Arbitrary for non-empty string values for text fields
    const nonEmptyStringArb = fc.string({minLength: 1, maxLength: 30}).filter(s => s.trim().length > 0);

    // Arbitrary for valid coordinate numbers
    const coordinateArb = fc.double({min: -180, max: 180, noNaN: true, noDefaultInfinity: true});

    it('should be valid if and only if all required fields are non-null and non-empty', () => {
      fc.assert(
        fc.property(
          requiredFieldPresenceArbitrary,
          nonEmptyStringArb,
          nonEmptyStringArb,
          fc.constantFrom('MALE', 'FEMALE'),
          fc.constantFrom('DOG', 'CAT'),
          coordinateArb,
          coordinateArb,
          (presence, nameVal, breedVal, genderVal, subtypeVal, lngVal, latVal) => {
            // Reset form to initial state
            component.form.reset({'details': {'@class': 'Pet'}});

            // Set each required field based on the presence map
            if (presence.name) {
              component.form.controls.details.controls.name.setValue(nameVal);
            }
            if (presence.breed) {
              component.form.controls.details.controls.breed.setValue(breedVal);
            }
            if (presence.gender) {
              component.form.controls.details.controls.gender.setValue(genderVal);
            }
            if (presence.subtype) {
              component.form.controls.details.controls.subtype.setValue(subtypeVal);
            }
            if (presence.lng) {
              component.form.controls.sightingDetails.controls.lng.setValue(lngVal);
            }
            if (presence.lat) {
              component.form.controls.sightingDetails.controls.lat.setValue(latVal);
            }

            component.form.updateValueAndValidity();

            const allPresent = presence.name && presence.breed && presence.gender
              && presence.subtype && presence.lng && presence.lat;

            expect(component.form.valid).toBe(allPresent);
          }
        ),
        {numRuns: 100}
      );
    });

    it('should disable the submit button when any required field is missing', () => {
      fc.assert(
        fc.property(
          requiredFieldPresenceArbitrary.filter(p =>
            !(p.name && p.breed && p.gender && p.subtype && p.lng && p.lat)
          ),
          nonEmptyStringArb,
          nonEmptyStringArb,
          fc.constantFrom('MALE', 'FEMALE'),
          fc.constantFrom('DOG', 'CAT'),
          coordinateArb,
          coordinateArb,
          (presence, nameVal, breedVal, genderVal, subtypeVal, lngVal, latVal) => {
            component.form.reset({'details': {'@class': 'Pet'}});

            if (presence.name) component.form.controls.details.controls.name.setValue(nameVal);
            if (presence.breed) component.form.controls.details.controls.breed.setValue(breedVal);
            if (presence.gender) component.form.controls.details.controls.gender.setValue(genderVal);
            if (presence.subtype) component.form.controls.details.controls.subtype.setValue(subtypeVal);
            if (presence.lng) component.form.controls.sightingDetails.controls.lng.setValue(lngVal);
            if (presence.lat) component.form.controls.sightingDetails.controls.lat.setValue(latVal);

            component.form.updateValueAndValidity();
            fixture.detectChanges();

            const submitButton = (fixture.nativeElement as HTMLElement).querySelector('button[type="submit"]') as HTMLButtonElement;
            expect(submitButton.disabled).toBeTrue();
          }
        ),
        {numRuns: 100}
      );
    });
  });

  // Feature: pet-content-creation-journey, Property 2: Map click coordinates are captured in the form model
  // Validates: Requirements 2.2
  describe('Property 2: Map click coordinates are captured in the form model', () => {
    let fixture: ComponentFixture<PetDetailsFormComponent>;
    let component: PetDetailsFormComponent;

    beforeEach(() => {
      const httpSpy = jasmine.createSpyObj('HttpClient', ['get', 'post']);

      TestBed.configureTestingModule({
        imports: [PetDetailsFormComponent],
        providers: [
          {provide: HttpClient, useValue: httpSpy},
          QueryGateway,
          CommandGateway,
          HandlerRegistry,
        ],
      }).overrideComponent(PetDetailsFormComponent, {
        remove: {imports: [MapboxLocationPickerComponent]},
        add: {imports: [MockMapboxLocationPickerComponent]},
      });

      fixture = TestBed.createComponent(PetDetailsFormComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    afterEach(() => {
      fixture.destroy();
    });

    // Arbitrary for longitude values (-180 to 180)
    const lngArb = fc.double({min: -180, max: 180, noNaN: true, noDefaultInfinity: true});

    // Arbitrary for latitude values (-90 to 90)
    const latArb = fc.double({min: -90, max: 90, noNaN: true, noDefaultInfinity: true});

    it('should store exact lng and lat values in sightingDetails when location is emitted', () => {
      fc.assert(
        fc.property(
          lngArb,
          latArb,
          (lng, lat) => {
            // Reset form to initial state
            component.form.reset({'details': {'@class': 'Pet'}});

            // Simulate MapboxLocationPickerComponent emitting coordinates
            component.onLocationSelected({lng, lat});

            // Assert the form model stores the exact coordinate values
            expect(component.form.controls.sightingDetails.controls.lng.value).toBe(lng);
            expect(component.form.controls.sightingDetails.controls.lat.value).toBe(lat);
          }
        ),
        {numRuns: 100}
      );
    });
  });

  // Feature: pet-content-creation-journey, Property 3: Valid form submission produces correct CreateContentDTO payload
  // Validates: Requirements 4.1
  describe('Property 3: Valid form submission produces correct CreateContentDTO payload', () => {
    let fixture: ComponentFixture<PetDetailsFormComponent>;
    let component: PetDetailsFormComponent;
    let httpSpy: jasmine.SpyObj<HttpClient>;
    let routerSpy: jasmine.SpyObj<Router>;

    beforeEach(() => {
      httpSpy = jasmine.createSpyObj('HttpClient', ['get', 'post']);
      routerSpy = jasmine.createSpyObj('Router', ['navigate']);

      TestBed.configureTestingModule({
        imports: [PetDetailsFormComponent],
        providers: [
          {provide: HttpClient, useValue: httpSpy},
          {provide: Router, useValue: routerSpy},
          QueryGateway,
          CommandGateway,
          HandlerRegistry,
        ],
      }).overrideComponent(PetDetailsFormComponent, {
        remove: {imports: [MapboxLocationPickerComponent]},
        add: {imports: [MockMapboxLocationPickerComponent]},
      });

      fixture = TestBed.createComponent(PetDetailsFormComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    afterEach(() => {
      fixture.destroy();
    });

    // Arbitraries for valid pet details
    const nonEmptyStringArb = fc.string({minLength: 1, maxLength: 30}).filter(s => s.trim().length > 0);
    const optionalStringArb = fc.option(fc.string({minLength: 1, maxLength: 30}), {nil: null});
    const lngArb = fc.double({min: -180, max: 180, noNaN: true, noDefaultInfinity: true});
    const latArb = fc.double({min: -90, max: 90, noNaN: true, noDefaultInfinity: true});

    const validPetDetailsArb = fc.record({
      name: nonEmptyStringArb,
      breed: nonEmptyStringArb,
      gender: fc.constantFrom('MALE', 'FEMALE'),
      subtype: fc.constantFrom('DOG', 'CAT'),
      age: optionalStringArb,
      size: optionalStringArb,
      color: optionalStringArb,
      condition: optionalStringArb,
      description: optionalStringArb,
      location: optionalStringArb,
      image: optionalStringArb,
    });

    it('should POST to /api/content with payload matching form values and @class set to "Pet"', () => {
      fc.assert(
        fc.property(
          validPetDetailsArb,
          lngArb,
          latArb,
          (petDetails, lng, lat) => {
            // Arrange: make HttpClient.post return a fake ContentId
            httpSpy.post.calls.reset();
            httpSpy.post.and.returnValue(of('fake-content-id'));

            // Reset form and populate with generated values
            component.form.reset({'details': {'@class': 'Pet'}});

            component.form.controls.sightingDetails.controls.lng.setValue(lng);
            component.form.controls.sightingDetails.controls.lat.setValue(lat);
            component.form.controls.details.controls.name.setValue(petDetails.name);
            component.form.controls.details.controls.breed.setValue(petDetails.breed);
            component.form.controls.details.controls.gender.setValue(petDetails.gender);
            component.form.controls.details.controls.subtype.setValue(petDetails.subtype);
            component.form.controls.details.controls.age.setValue(petDetails.age);
            component.form.controls.details.controls.size.setValue(petDetails.size);
            component.form.controls.details.controls.color.setValue(petDetails.color);
            component.form.controls.details.controls.condition.setValue(petDetails.condition);
            component.form.controls.details.controls.description.setValue(petDetails.description);
            component.form.controls.details.controls.location.setValue(petDetails.location);
            component.form.controls.details.controls.image.setValue(petDetails.image);

            component.form.updateValueAndValidity();

            // Act: submit the form
            component.onSubmit();

            // Assert: HttpClient.post was called
            expect(httpSpy.post).toHaveBeenCalledTimes(1);

            const [url, payload] = httpSpy.post.calls.mostRecent().args;

            // Assert: correct URL
            expect(url).toBe('/api/content');

            // Assert: sightingDetails match form coordinates
            expect(payload.sightingDetails.lng).toBe(lng);
            expect(payload.sightingDetails.lat).toBe(lat);

            // Assert: details contains @class set to "Pet"
            expect(payload.details['@class']).toBe('Pet');

            // Assert: all pet fields match form values
            expect(payload.details.name).toBe(petDetails.name);
            expect(payload.details.breed).toBe(petDetails.breed);
            expect(payload.details.gender).toBe(petDetails.gender);
            expect(payload.details.subtype).toBe(petDetails.subtype);
            expect(payload.details.age).toBe(petDetails.age);
            expect(payload.details.size).toBe(petDetails.size);
            expect(payload.details.color).toBe(petDetails.color);
            expect(payload.details.condition).toBe(petDetails.condition);
            expect(payload.details.description).toBe(petDetails.description);
            expect(payload.details.location).toBe(petDetails.location);
            expect(payload.details.image).toBe(petDetails.image);
          }
        ),
        {numRuns: 100}
      );
    });
  });

  // Feature: pet-content-creation-journey, Unit Tests: Pet details form rendering and behavior
  // Validates: Requirements 1.1, 1.3, 1.4, 2.1, 4.4, 4.5
  describe('Unit Tests: Pet details form rendering and behavior', () => {
    let fixture: ComponentFixture<PetDetailsFormComponent>;
    let component: PetDetailsFormComponent;
    let httpSpy: jasmine.SpyObj<HttpClient>;
    let routerSpy: jasmine.SpyObj<Router>;

    beforeEach(() => {
      httpSpy = jasmine.createSpyObj('HttpClient', ['get', 'post']);
      routerSpy = jasmine.createSpyObj('Router', ['navigate']);

      TestBed.configureTestingModule({
        imports: [PetDetailsFormComponent],
        providers: [
          {provide: HttpClient, useValue: httpSpy},
          {provide: Router, useValue: routerSpy},
          QueryGateway,
          CommandGateway,
          HandlerRegistry,
        ],
      }).overrideComponent(PetDetailsFormComponent, {
        remove: {imports: [MapboxLocationPickerComponent]},
        add: {imports: [MockMapboxLocationPickerComponent]},
      });

      fixture = TestBed.createComponent(PetDetailsFormComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    afterEach(() => {
      fixture.destroy();
    });

    it('should render all expected form fields', () => {
      const el: HTMLElement = fixture.nativeElement;

      // Required fields
      expect(el.querySelector('input[formControlName="name"]')).toBeTruthy();
      expect(el.querySelector('input[formControlName="breed"]')).toBeTruthy();
      expect(el.querySelector('mat-select[formControlName="gender"]')).toBeTruthy();
      expect(el.querySelector('mat-select[formControlName="subtype"]')).toBeTruthy();

      // Optional fields
      expect(el.querySelector('input[formControlName="age"]')).toBeTruthy();
      expect(el.querySelector('input[formControlName="size"]')).toBeTruthy();
      expect(el.querySelector('input[formControlName="color"]')).toBeTruthy();
      expect(el.querySelector('input[formControlName="condition"]')).toBeTruthy();
      expect(el.querySelector('textarea[formControlName="description"]')).toBeTruthy();
      expect(el.querySelector('input[formControlName="image"]')).toBeTruthy();
    });

    it('should have gender select with MALE and FEMALE options', async () => {
      const el: HTMLElement = fixture.nativeElement;
      const genderSelect = el.querySelector('mat-select[formControlName="gender"]') as HTMLElement;
      genderSelect.click();
      fixture.detectChanges();
      await fixture.whenStable();

      const options = document.querySelectorAll('mat-option');
      const optionTexts = Array.from(options).map(o => o.textContent?.trim());
      expect(optionTexts).toContain('Male');
      expect(optionTexts).toContain('Female');
      expect(options.length).toBe(2);
    });

    it('should have subtype select with DOG and CAT options', async () => {
      const el: HTMLElement = fixture.nativeElement;
      const subtypeSelect = el.querySelector('mat-select[formControlName="subtype"]') as HTMLElement;
      subtypeSelect.click();
      fixture.detectChanges();
      await fixture.whenStable();

      const options = document.querySelectorAll('mat-option');
      const optionTexts = Array.from(options).map(o => o.textContent?.trim());
      expect(optionTexts).toContain('Dog');
      expect(optionTexts).toContain('Cat');
      expect(options.length).toBe(2);
    });

    it('should render the map picker component', () => {
      const el: HTMLElement = fixture.nativeElement;
      expect(el.querySelector('track-rejoice-mapbox-location-picker')).toBeTruthy();
    });

    it('should show loading indicator during submission', () => {
      const el: HTMLElement = fixture.nativeElement;

      // Initially no loading indicator
      expect(el.querySelector('.loading-indicator')).toBeFalsy();

      // Simulate submitting state
      component.submitting.set(true);
      fixture.detectChanges();

      const loadingEl = el.querySelector('.loading-indicator');
      expect(loadingEl).toBeTruthy();
      expect(loadingEl.textContent).toContain('Creating content');
    });

    it('should display error message on submission failure', () => {
      const el: HTMLElement = fixture.nativeElement;

      // Initially no error message
      const errorsBefore = Array.from(el.querySelectorAll('.control-error'))
        .filter(e => e.textContent.includes('Failed to create'));
      expect(errorsBefore.length).toBe(0);

      // Set error message
      component.errorMessage.set('Failed to create content. Please try again.');
      fixture.detectChanges();

      const errorsAfter = Array.from(el.querySelectorAll('.control-error'))
        .filter(e => e.textContent.includes('Failed to create content'));
      expect(errorsAfter.length).toBe(1);
    });
  });

  // Feature: pet-content-creation-journey, Property 4: Successful content creation navigates to payment with ContentId
  // Validates: Requirements 5.1
  describe('Property 4: Successful content creation navigates to payment with ContentId', () => {
    let fixture: ComponentFixture<PetDetailsFormComponent>;
    let component: PetDetailsFormComponent;
    let httpSpy: jasmine.SpyObj<HttpClient>;
    let routerSpy: jasmine.SpyObj<Router>;

    beforeEach(() => {
      httpSpy = jasmine.createSpyObj('HttpClient', ['get', 'post']);
      routerSpy = jasmine.createSpyObj('Router', ['navigate']);

      TestBed.configureTestingModule({
        imports: [PetDetailsFormComponent],
        providers: [
          {provide: HttpClient, useValue: httpSpy},
          {provide: Router, useValue: routerSpy},
          QueryGateway,
          CommandGateway,
          HandlerRegistry,
        ],
      }).overrideComponent(PetDetailsFormComponent, {
        remove: {imports: [MapboxLocationPickerComponent]},
        add: {imports: [MockMapboxLocationPickerComponent]},
      });

      fixture = TestBed.createComponent(PetDetailsFormComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    afterEach(() => {
      fixture.destroy();
    });

    // Arbitrary for ContentId strings: non-empty alphanumeric with hyphens (UUID-like)
    const contentIdArb = fc.stringMatching(/^[a-zA-Z0-9-]{1,64}$/);

    it('should navigate to /content/new/payment/{contentId} on successful content creation', () => {
      fc.assert(
        fc.property(
          contentIdArb,
          (contentId) => {
            // Arrange: mock HttpClient.post to return the generated ContentId
            httpSpy.post.calls.reset();
            routerSpy.navigate.calls.reset();
            httpSpy.post.and.returnValue(of(contentId));

            // Fill form with valid values so submission proceeds
            component.form.reset({'details': {'@class': 'Pet'}});
            component.form.controls.details.controls.name.setValue('Buddy');
            component.form.controls.details.controls.breed.setValue('Labrador');
            component.form.controls.details.controls.gender.setValue('MALE');
            component.form.controls.details.controls.subtype.setValue('DOG');
            component.form.controls.sightingDetails.controls.lng.setValue(5.0);
            component.form.controls.sightingDetails.controls.lat.setValue(52.0);
            component.form.updateValueAndValidity();

            // Act: submit the form
            component.onSubmit();

            // Assert: Router.navigate was called with the correct payment route
            expect(routerSpy.navigate).toHaveBeenCalledTimes(1);
            expect(routerSpy.navigate).toHaveBeenCalledWith(
              ['/content/new/payment', contentId],
              {replaceUrl: true}
            );
          }
        ),
        {numRuns: 100}
      );
    });
  });
});
