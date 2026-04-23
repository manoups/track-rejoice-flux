import {Component, OnDestroy, OnInit, signal} from '@angular/core';
import {NavigationEnd, Router, RouterOutlet} from '@angular/router';
import {filter, Subscription} from 'rxjs';
import {StepIndicatorComponent} from './step-indicator/step-indicator.component';

@Component({
  selector: 'track-rejoice-content-creation-journey',
  imports: [RouterOutlet, StepIndicatorComponent],
  templateUrl: './content-creation-journey.component.html',
  styleUrl: './content-creation-journey.component.css',
})
export class ContentCreationJourneyComponent implements OnInit, OnDestroy {
  currentStep = signal(1);
  private routerSubscription: Subscription;

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.updateStep(this.router.url);
    this.routerSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => this.updateStep(event.urlAfterRedirects));
  }

  ngOnDestroy(): void {
    this.routerSubscription?.unsubscribe();
  }

  private updateStep(url: string): void {
    this.currentStep.set(url.includes('/payment/') ? 2 : 1);
  }
}
