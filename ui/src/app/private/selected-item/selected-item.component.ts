import {Component, input} from '@angular/core';
import {SightingDocument} from '@trackrejoice/typescriptmodels';
import {MatDrawer, MatDrawerContainer, MatDrawerContent} from '@angular/material/sidenav';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'track-rejoice-selected-item',
  imports: [
    MatDrawerContainer,
    MatDrawer,
    MatDrawerContent,
    MatButton
  ],
  templateUrl: './selected-item.component.html',
  styleUrl: './selected-item.component.css',
})
export class SelectedItemComponent {
  selectedItem = input<SightingDocument>();
}
