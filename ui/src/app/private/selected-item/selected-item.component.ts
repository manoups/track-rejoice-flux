import {Component, input} from '@angular/core';
import {SightingDocument} from '@trackrejoice/typescriptmodels';
import {MatDrawer, MatDrawerContainer, MatDrawerContent} from '@angular/material/sidenav';
import {MatButton, MatFabButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'track-rejoice-selected-item',
  imports: [
    MatDrawerContainer,
    MatDrawer,
    MatDrawerContent,
    MatButton,
    MatFabButton,
    MatIcon
  ],
  templateUrl: './selected-item.component.html',
  styleUrl: './selected-item.component.css',
})
export class SelectedItemComponent {
  selectedItem = input<SightingDocument>();
}
