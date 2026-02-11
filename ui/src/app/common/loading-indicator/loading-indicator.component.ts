import {Component, ContentChild, input, TemplateRef} from '@angular/core';
import {RouteConfigLoadEnd, RouteConfigLoadStart, Router} from '@angular/router';
import {LoadingService} from '../loading.service';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {AsyncPipe, NgTemplateOutlet} from '@angular/common';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';

@Component({
  selector: 'track-rejoice-loading-indicator',
  imports: [MatProgressSpinnerModule, AsyncPipe, NgTemplateOutlet],
  templateUrl: './loading-indicator.component.html',
  styleUrl: './loading-indicator.component.css',
})
export class LoadingIndicatorComponent {
  detectRouteTransitions = input<boolean>(false);
  loading$ : Observable<boolean>;
  @ContentChild("loading")
  customLoadingIndicator: TemplateRef<any> | null = null;

  constructor(
    private loadingService: LoadingService,
    private router: Router) {
    this.loading$ = this.loadingService.loading$;
  }

  ngOnInit() {
    if (this.detectRouteTransitions) {
      this.router.events
        .pipe(
          tap((event) => {
            if (event instanceof RouteConfigLoadStart) {
              this.loadingService.loadingOn();
            } else if (event instanceof RouteConfigLoadEnd) {
              this.loadingService.loadingOff();
            }
          })
        )
        .subscribe();
    }
  }
}
