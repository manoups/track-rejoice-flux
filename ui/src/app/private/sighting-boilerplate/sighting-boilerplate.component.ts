import {Component, signal} from '@angular/core';
import {Subject} from 'rxjs';
import {FacetFilter, GetFacetStatsResult} from '@trackrejoice/typescriptmodels';
import {FilterSidebarComponent} from '../filter-sidebar/filter-sidebar.component';
import {FormsModule} from '@angular/forms';
import {SightingsComponent} from '../sightings/sightings.component';
import {MatToolbar} from '@angular/material/toolbar';
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from '@angular/material/sidenav';

@Component({
  selector: 'track-rejoice-sighting-boilerplate',
  imports: [
    FilterSidebarComponent,
    FormsModule,
    SightingsComponent,
    MatToolbar,
    MatSidenav,
    MatSidenavContainer,
    MatSidenavContent
  ],
  templateUrl: './sighting-boilerplate.component.html',
  styleUrl: './sighting-boilerplate.component.css',
})
export class SightingBoilerplateComponent {
  loading = signal(false);
  filterChange$ = new Subject<[string, FacetFilter[]]>();
  facetFields: GetFacetStatsResult = {'@type': 'GetFacetStatsResult', stats: {"type": [{"value": "dog", "count": 0}, {"value": "cat", "count": 0}, {"value": "keys", "count": 0}]}} as GetFacetStatsResult;

  onFilterChange($event: [string, FacetFilter[]]): void {
    console.log('onFilterChange', $event);
    this.filterChange$.next($event);
  }
}
