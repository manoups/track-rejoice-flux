import {Component, input} from '@angular/core';
import {SightingDocument} from '@trackrejoice/typescriptmodels';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'track-rejoice-sighting-basic-details',
  imports: [
    DatePipe
  ],
  templateUrl: './sighting-basic-details.component.html',
  styleUrl: './sighting-basic-details.component.css',
})
export class SightingBasicDetailsComponent {
  sighting= input.required<SightingDocument>();

}
