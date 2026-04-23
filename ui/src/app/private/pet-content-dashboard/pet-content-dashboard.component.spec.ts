import * as fc from 'fast-check';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {Component, input} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {of} from 'rxjs';
import {Content, SightingDetails} from '@trackrejoice/typescriptmodels';
import {PetContentDashboardComponent} from './pet-content-dashboard.component';
import {ContentDetailsPanelComponent} from './content-details-panel/content-details-panel.component';
import {SightingHistoryMapComponent} from './sighting-history-map/sighting-history-map.component';
import {By} from '@angular/platform-browser';

@Component({
  selector: 'track-rejoice-sighting-history-map',
  template: '',
})
class MockSightingHistoryMapComponent {
  contentId = input.required<string>();
  lastConfirmedSighting = input<SightingDetails | null>(null);
}

@Component({
  selector: 'track-rejoice-content-details-panel',
  template: '',
})
class MockContentDetailsPanelComponent {
  content = input.required<Content>();
}

@Component({
  selector: 'track-rejoice-proposals-table',
  template: '',
})
class MockProposalsTableComponent {
  contentId = input.required<string>();
}

describe('PetContentDashboardComponent', () => {

  // Feature: pet-content-sse-dashboard, Property 5: Route parameter propagates to all dashboard panels
  // Validates: Requirements 3.2
  describe('Property 5: Route parameter propagates to all dashboard panels', () => {
    let httpSpy: jasmine.SpyObj<HttpClient>;

    beforeEach(() => {
      httpSpy = jasmine.createSpyObj('HttpClient', ['get']);

      TestBed.configureTestingModule({
        imports: [PetContentDashboardComponent],
        providers: [
          {provide: HttpClient, useValue: httpSpy},
        ],
      })
        .overrideComponent(PetContentDashboardComponent, {
          set: {
            imports: [MockContentDetailsPanelComponent, MockSightingHistoryMapComponent, MockProposalsTableComponent],
          },
        });
    });

    it('should pass the route id to sighting-history-map and proposals-table as contentId', () => {
      fc.assert(
        fc.property(
          fc.string({minLength: 1, maxLength: 50}).filter(s => s.trim().length > 0),
          (contentId: string) => {
            const mockContent: Content = {
              contentId: contentId,
              online: true,
              duration: 'P30D',
              ownerId: 'owner-1',
              details: {'@class': 'Pet', name: 'Buddy', breed: 'Labrador'} as any,
            };
            httpSpy.get.and.returnValue(of(mockContent));

            const fixture: ComponentFixture<PetContentDashboardComponent> = TestBed.createComponent(PetContentDashboardComponent);
            fixture.componentRef.setInput('id', contentId);
            fixture.detectChanges();

            // Verify the component's id signal holds the correct value
            expect(fixture.componentInstance.id()).toBe(contentId);

            // Verify SightingHistoryMapComponent receives contentId via its signal input
            const mapDebugEl = fixture.debugElement.query(By.directive(MockSightingHistoryMapComponent));
            expect(mapDebugEl).toBeTruthy();
            expect(mapDebugEl.componentInstance.contentId()).toBe(contentId);

            // Verify ProposalsTableComponent receives contentId
            const proposalsDebugEl = fixture.debugElement.query(By.directive(MockProposalsTableComponent));
            expect(proposalsDebugEl).toBeTruthy();
            expect(proposalsDebugEl.componentInstance.contentId()).toBe(contentId);

            fixture.destroy();
          }
        ),
        {numRuns: 20}
      );
    });
  });
});
