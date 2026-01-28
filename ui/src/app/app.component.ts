import {Component} from '@angular/core';
import {SightingsComponent} from './private/sightings/sightings.component';

@Component({
  selector: 'track-rejoice-root',
  imports: [SightingsComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'track-rejoice-ui';
}
