import {Component, input, signal} from '@angular/core';
import {Subject} from 'rxjs';
import {FacetFilter} from '@trackrejoice/typescriptmodels';
import {FormsModule} from '@angular/forms';
import {SightingsComponent} from '../sightings/sightings.component';
import {MatToolbar} from '@angular/material/toolbar';
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from '@angular/material/sidenav';
import {SidebarComponent} from '../sidebar/sidebar.component';
import {MatIcon} from '@angular/material/icon';
import {MatMiniFabButton} from '@angular/material/button';

@Component({
  selector: 'track-rejoice-sighting-boilerplate',
  imports: [
    FormsModule,
    SightingsComponent,
    MatToolbar,
    MatSidenav,
    MatSidenavContainer,
    MatSidenavContent,
    SidebarComponent,
    MatIcon,
    MatMiniFabButton
  ],
  templateUrl: './sighting-boilerplate.component.html',
  styleUrl: './sighting-boilerplate.component.css',
})
export class SightingBoilerplateComponent {
  loading = signal(false);
  statsEndpoint = input.required<string>();
  filterChange$ = new Subject<[string, FacetFilter[]]>();
  events: string[] = [];
  opened = true;
}
