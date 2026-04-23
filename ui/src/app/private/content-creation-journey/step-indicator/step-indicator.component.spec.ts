import {ComponentFixture, TestBed} from '@angular/core/testing';
import {StepIndicatorComponent} from './step-indicator.component';
import {By} from '@angular/platform-browser';

describe('StepIndicatorComponent', () => {
  let component: StepIndicatorComponent;
  let fixture: ComponentFixture<StepIndicatorComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [StepIndicatorComponent],
    });

    fixture = TestBed.createComponent(StepIndicatorComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should display two steps: "Pet Details" and "Payment"', () => {
    fixture.detectChanges();
    const stepLabels = fixture.debugElement
      .queryAll(By.css('ol > li'))
      .filter(el => el.nativeElement.textContent.trim().length > 0)
      .map(el => el.nativeElement.textContent.replace(/\s+/g, ' ').trim());

    expect(stepLabels).toContain('1 Pet Details');
    expect(stepLabels).toContain('2 Payment');
  });

  it('should highlight step 1 as active when currentStep is 1', () => {
    component.currentStep = 1;
    fixture.detectChanges();

    const badges = fixture.debugElement.queryAll(By.css('.badge'));
    expect(badges[0].nativeElement.classList).toContain('bg-primary');
    expect(badges[1].nativeElement.classList).toContain('bg-light');
  });

  it('should highlight step 2 as active when currentStep is 2', () => {
    component.currentStep = 2;
    fixture.detectChanges();

    const badges = fixture.debugElement.queryAll(By.css('.badge'));
    expect(badges[0].nativeElement.classList).toContain('bg-primary');
    expect(badges[1].nativeElement.classList).toContain('bg-primary');
  });

  it('should set aria-current="step" on the active step badge', () => {
    component.currentStep = 1;
    fixture.detectChanges();

    const badges = fixture.debugElement.queryAll(By.css('.badge'));
    expect(badges[0].nativeElement.getAttribute('aria-current')).toBe('step');
    expect(badges[1].nativeElement.getAttribute('aria-current')).toBeNull();
  });

  it('should apply fw-semibold to the current step label', () => {
    component.currentStep = 2;
    fixture.detectChanges();

    const labels = fixture.debugElement.queryAll(By.css('ol > li > span:not(.badge)'));
    expect(labels[1].nativeElement.classList).toContain('fw-semibold');
  });

  it('should show a connecting line between steps', () => {
    fixture.detectChanges();
    const hr = fixture.debugElement.query(By.css('hr'));
    expect(hr).toBeTruthy();
  });

  it('should highlight the connecting line when on step 2', () => {
    component.currentStep = 2;
    fixture.detectChanges();

    const hr = fixture.debugElement.query(By.css('hr'));
    expect(hr.nativeElement.classList).toContain('border-primary');
  });

  it('should default currentStep to 1', () => {
    expect(component.currentStep).toBe(1);
  });
});
