import {Component, inject, input, signal} from '@angular/core';
import {map} from 'rxjs';
import {
  FacetFilter,
  FacetPaginationRequestBody,
  GetFacetStatsResult,
  ValueCountPair
} from '@trackrejoice/typescriptmodels';
import {FormsModule} from '@angular/forms';
import {SightingsComponent} from '../sightings/sightings.component';
import {MatToolbar} from '@angular/material/toolbar';
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from '@angular/material/sidenav';
import {MatIcon} from '@angular/material/icon';
import {MatMiniFabButton} from '@angular/material/button';
import {ActivatedRouteSnapshot, ResolveFn, RouterStateSnapshot} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {SidebarNewComponent} from '../sidebar-new/sidebar-new.component';

@Component({
  selector: 'track-rejoice-sighting-boilerplate',
  imports: [
    FormsModule,
    SightingsComponent,
    MatToolbar,
    MatSidenav,
    MatSidenavContainer,
    MatSidenavContent,
    MatIcon,
    MatMiniFabButton,
    SidebarNewComponent
  ],
  templateUrl: './sighting-boilerplate.component.html',
  styleUrl: './sighting-boilerplate.component.css',
})
export class SightingBoilerplateComponent {
  loading = signal(false);
  statsEndpoint = input.required<string>();
  initFilterValues = input.required<Map<string, ValueCountPair[]>>();
  filterState = signal<[string, FacetFilter[]]>(['', []]);
  events: string[] = [];
  opened = true;
  updateFilter = (value: [string, FacetFilter[]]) => {
    this.filterState.set(value);
  };
}

export const resolveFilters: ResolveFn<Map<string, ValueCountPair[]>> = (activatedRoute: ActivatedRouteSnapshot, routerState: RouterStateSnapshot) => {
  const http = inject(HttpClient);
  const initBody: FacetPaginationRequestBody = {facetFilters: [], filter: "", pagination: {page: 0, pageSize: 10}}
  return http.post<GetFacetStatsResult>(activatedRoute.data["statsEndpoint"], initBody, {withCredentials: true}).pipe(
    map(facetResults => facetResults.stats),
    map(facetResults => {
      const filterMap = new Map<string, ValueCountPair[]>();
      for (const [facetName, allowedPairs] of Object.entries(facetResults ?? {})) {
        filterMap.set(facetName, allowedPairs);
      }
      return filterMap;
    }))
}
