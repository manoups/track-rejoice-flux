import {Component, inject, input, signal} from '@angular/core';
import {map, Observable, Subject, take} from 'rxjs';
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
import {SidebarComponent} from '../sidebar/sidebar.component';
import {MatIcon} from '@angular/material/icon';
import {MatMiniFabButton} from '@angular/material/button';
import {ActivatedRouteSnapshot, ResolveFn, RouterStateSnapshot} from '@angular/router';
import {HttpClient} from '@angular/common/http';

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
  initFilterValues = input.required<Map<string, ValueCountPair[]>>();
  filterChange$ = new Subject<[string, FacetFilter[]]>();
  events: string[] = [];
  opened = true;
}

export const resolveFilters: ResolveFn<Observable<Map<string, ValueCountPair[]>>> = (activatedRoute: ActivatedRouteSnapshot, routerState: RouterStateSnapshot) => {
  const http = inject(HttpClient);
  const initBody: FacetPaginationRequestBody = {facetFilters: [], filter: "", pagination: {page: 0, pageSize: 10}}
  return http.post<GetFacetStatsResult>(activatedRoute.data["statsEndpoint"], initBody, {withCredentials: true}).pipe(
    map(facetResults => facetResults.stats), take(1),
    map(facetResults => {
      const filterMap: Map<string, ValueCountPair[]> = new Map();
      for (const [facetName, allowedPairs] of Object.entries(facetResults ?? {})) {
        filterMap.set(facetName, allowedPairs);
      }
      return filterMap;
    }))
}
