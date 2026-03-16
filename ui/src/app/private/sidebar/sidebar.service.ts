import {inject, Injectable} from '@angular/core';
import {map} from 'rxjs';
import {FacetPaginationRequestBody, GetFacetStatsResult} from '@trackrejoice/typescriptmodels';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class SidebarService {
  http = inject(HttpClient);

  getStats(filter, facet, statsEndpoint: string) {
    const body: FacetPaginationRequestBody = {
      facetFilters: facet,
      filter,
      pagination: {page: 0, pageSize: 10}
    };

    return this.http.post<GetFacetStatsResult>(statsEndpoint, body, {withCredentials: true}).pipe(
      map(result => result.stats),
      // map(stats => this.mergeWithInitialValues(stats))
    );
  }
}
