import {Component, signal} from '@angular/core';
import {Subject} from 'rxjs';
import {FacetFilter} from '@trackrejoice/typescriptmodels';
import {FilterSidebarComponent} from '../filter-sidebar/filter-sidebar.component';
import {FormsModule} from '@angular/forms';
import {SightingsComponent} from '../sightings/sightings.component';

@Component({
  selector: 'track-rejoice-sighting-boilerplate',
  imports: [
    FilterSidebarComponent,
    FormsModule,
    SightingsComponent
  ],
  templateUrl: './sighting-boilerplate.component.html',
  styleUrl: './sighting-boilerplate.component.css',
})
export class SightingBoilerplateComponent {
  loading = signal(false);
  filterChange$ = new Subject<[string, FacetFilter[]]>();

  onFilterChange($event: [string, FacetFilter[]]): void {
    console.log('onFilterChange', $event);
    this.filterChange$.next($event);
  }
}
