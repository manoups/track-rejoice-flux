import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {LoadingIndicatorComponent} from './common/loading-indicator/loading-indicator.component';

@Component({
  selector: 'track-rejoice-root',
  imports: [
    RouterOutlet,
    LoadingIndicatorComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'track-rejoice-ui';
}
