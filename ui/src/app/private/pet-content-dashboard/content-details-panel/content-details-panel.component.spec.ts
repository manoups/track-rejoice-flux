import * as fc from 'fast-check';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {Content, Pet} from '@trackrejoice/typescriptmodels';
import {ContentDetailsPanelComponent} from './content-details-panel.component';

// Arbitrary for generating random Pet objects
const petArbitrary = fc.record({
  '@class': fc.constant('Pet' as const),
  name: fc.string({minLength: 1, maxLength: 30}).filter(s => s.trim().length > 0),
  breed: fc.string({minLength: 1, maxLength: 30}).filter(s => s.trim().length > 0),
  gender: fc.constantFrom('MALE' as const, 'FEMALE' as const),
  age: fc.string({minLength: 1, maxLength: 10}).filter(s => s.trim().length > 0),
  size: fc.string({minLength: 1, maxLength: 20}).filter(s => s.trim().length > 0),
  color: fc.string({minLength: 1, maxLength: 20}).filter(s => s.trim().length > 0),
  condition: fc.string({minLength: 1, maxLength: 30}).filter(s => s.trim().length > 0),
  description: fc.string({minLength: 1, maxLength: 100}).filter(s => s.trim().length > 0),
  image: fc.option(
    fc.webUrl().filter(u => u.length > 0),
    {nil: undefined}
  ),
}) as fc.Arbitrary<Pet>;

// Arbitrary for generating random Content objects with Pet details
const contentWithPetArbitrary = fc.record({
  contentId: fc.string({minLength: 1, maxLength: 30}),
  online: fc.boolean(),
  duration: fc.string({minLength: 1, maxLength: 10}).filter(s => s.trim().length > 0),
  ownerId: fc.string({minLength: 1, maxLength: 20}),
  details: petArbitrary,
}) as fc.Arbitrary<Content>;

describe('ContentDetailsPanelComponent', () => {

  // Feature: pet-content-sse-dashboard, Property 6: Content details panel renders all required fields
  // Validates: Requirements 4.2, 4.3, 4.4, 4.6
  describe('Property 6: Content details panel renders all required fields', () => {

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ContentDetailsPanelComponent],
      });
    });

    it('should render all pet fields, online status, and duration for any Content with Pet details', () => {
      fc.assert(
        fc.property(
          contentWithPetArbitrary,
          (content: Content) => {
            const fixture: ComponentFixture<ContentDetailsPanelComponent> = TestBed.createComponent(ContentDetailsPanelComponent);
            fixture.componentRef.setInput('content', content);
            fixture.detectChanges();

            const el: HTMLElement = fixture.nativeElement;
            const pet = content.details as Pet;

            // Verify pet-specific fields are rendered
            const textContent = el.textContent ?? '';
            expect(textContent).toContain(pet.name!);
            expect(textContent).toContain(pet.breed!);
            expect(textContent).toContain(pet.gender!);
            expect(textContent).toContain(pet.age!);
            expect(textContent).toContain(pet.size!);
            expect(textContent).toContain(pet.color!);
            expect(textContent).toContain(pet.condition!);
            expect(textContent).toContain(pet.description!);

            // Verify content-level fields
            expect(textContent).toContain(String(content.online));
            expect(textContent).toContain(content.duration!);

            fixture.destroy();
          }
        ),
        {numRuns: 20}
      );
    });

    it('should render image with alt text containing pet name and breed when image is non-null', () => {
      fc.assert(
        fc.property(
          contentWithPetArbitrary.filter(c => !!(c.details as Pet).image),
          (content: Content) => {
            const fixture: ComponentFixture<ContentDetailsPanelComponent> = TestBed.createComponent(ContentDetailsPanelComponent);
            fixture.componentRef.setInput('content', content);
            fixture.detectChanges();

            const el: HTMLElement = fixture.nativeElement;
            const pet = content.details as Pet;
            const img = el.querySelector('img');

            expect(img).toBeTruthy();
            const altText = img!.getAttribute('alt') ?? '';
            expect(altText).toContain(pet.name!);
            expect(altText).toContain(pet.breed!);

            fixture.destroy();
          }
        ),
        {numRuns: 20}
      );
    });
  });
});
