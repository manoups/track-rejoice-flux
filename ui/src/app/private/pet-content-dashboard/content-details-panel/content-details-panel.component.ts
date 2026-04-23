import {Component, input} from '@angular/core';
import {Content, Pet} from '@trackrejoice/typescriptmodels';

@Component({
  selector: 'track-rejoice-content-details-panel',
  imports: [],
  templateUrl: './content-details-panel.component.html',
  styleUrl: './content-details-panel.component.css',
})
export class ContentDetailsPanelComponent {
  content = input.required<Content>();

  isPet(): boolean {
    return this.content().details?.['@class'] === 'Pet';
  }

  get pet(): Pet {
    return this.content().details as Pet;
  }
}
