import {Component, input} from '@angular/core';
import {SightingDocument} from '@trackrejoice/typescriptmodels';
import {MatDrawer, MatDrawerContainer, MatDrawerContent} from '@angular/material/sidenav';
import {MatButton, MatMiniFabButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {RouterLink} from '@angular/router';
import {SightingBasicDetailsComponent} from '../sighting-basic-details/sighting-basic-details.component';

@Component({
  selector: 'track-rejoice-selected-item',
  imports: [
    MatDrawerContainer,
    MatDrawer,
    MatDrawerContent,
    MatIcon,
    MatButton,
    RouterLink,
    MatMiniFabButton,
    SightingBasicDetailsComponent
  ],
  templateUrl: './selected-item.component.html',
  styleUrl: './selected-item.component.css',
})
export class SelectedItemComponent {
  selectedItem = input<SightingDocument>();
}
