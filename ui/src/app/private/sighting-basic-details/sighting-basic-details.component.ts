import {Component, input} from '@angular/core';
import {SightingDocument} from '@trackrejoice/typescriptmodels';

@Component({
  selector: 'track-rejoice-sighting-basic-details',
  imports: [],
  templateUrl: './sighting-basic-details.component.html',
  styleUrl: './sighting-basic-details.component.css',
})
export class SightingBasicDetailsComponent {
  sighting= input.required<SightingDocument>();

}
