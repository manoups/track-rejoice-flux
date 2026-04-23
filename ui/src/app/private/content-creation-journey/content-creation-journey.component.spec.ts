import {ComponentFixture, TestBed} from '@angular/core/testing';
import {Router, NavigationEnd, RouterOutlet} from '@angular/router';
import {Subject} from 'rxjs';
import {ContentCreationJourneyComponent} from './content-creation-journey.component';
import {StepIndicatorComponent} from './step-indicator/step-indicator.component';
import {By} from '@angular/platform-browser';

describe('ContentCreationJourneyComponent', () => {
  let component: ContentCreationJourneyComponent;
  let fixture: ComponentFixture<ContentCreationJourneyComponent>;
  let routerEvents$: Subject<any>;
  let mockRouter: { events: Subject<any>; url: string };

  beforeEach(() => {
    routerEvents$ = new Subject();
    mockRouter = {events: routerEvents$, url: '/content/new'};

    TestBed.configureTestingModule({
      imports: [ContentCreationJourneyComponent],
      providers: [
        {provide: Router, useValue: mockRouter},
      ],
    }).overrideComponent(ContentCreationJourneyComponent, {
      set: {imports: [RouterOutlet, StepIndicatorComponent]},
    });

    fixture = TestBed.createComponent(ContentCreationJourneyComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should set currentStep to 1 on init when URL does not contain /payment/', () => {
    mockRouter.url = '/content/new';
    fixture.detectChanges();
    expect(component.currentStep()).toBe(1);
  });

  it('should set currentStep to 2 on init when URL contains /payment/', () => {
    mockRouter.url = '/content/new/payment/abc-123';
    fixture.detectChanges();
    expect(component.currentStep()).toBe(2);
  });

  it('should update currentStep to 2 when navigating to payment route', () => {
    fixture.detectChanges();
    expect(component.currentStep()).toBe(1);

    routerEvents$.next(new NavigationEnd(1, '/content/new/payment/xyz', '/content/new/payment/xyz'));
    expect(component.currentStep()).toBe(2);
  });

  it('should update currentStep to 1 when navigating back from payment route', () => {
    mockRouter.url = '/content/new/payment/abc';
    fixture.detectChanges();
    expect(component.currentStep()).toBe(2);

    routerEvents$.next(new NavigationEnd(2, '/content/new', '/content/new'));
    expect(component.currentStep()).toBe(1);
  });

  it('should contain a router-outlet', () => {
    fixture.detectChanges();
    const outlet = fixture.debugElement.query(By.directive(RouterOutlet));
    expect(outlet).toBeTruthy();
  });
});
