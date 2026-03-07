// mock-backend.interceptor.ts
import {HttpEvent, HttpHandlerFn, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable, of, throwError} from 'rxjs';
import {delay} from 'rxjs/operators';
import {FacetPaginationRequestBody, SightingDocument} from '@trackrejoice/typescriptmodels';
import {mockSightings, mockUserIds} from './sightings.mock';

let userIds = mockUserIds;

let sightingDocuments: SightingDocument[] = mockSightings;

export function mockBackendInterceptor(
  req: HttpRequest<any>,
  next: HttpHandlerFn
): Observable<HttpEvent<any>> {

  const {method, url, params, body} = req;

  if (!url.startsWith('/api/sighting') || !url.endsWith('/list')) {
    return next(req);
  }

  // POST
  if (method === 'POST') {
    let typedBody:FacetPaginationRequestBody = req.body;
    let result = typedBody.filter?.trim().length > 0 ? [...mockSightings.filter(val => val.sightingId.includes(typedBody.filter))] : [...mockSightings];
    if(typedBody.facetFilters && typedBody.facetFilters.length > 0){
      result = result.filter(it => typedBody.facetFilters.every(ff => ff.values.some(it2 => it[ff.facetName].toLowerCase()==it2.toLowerCase())));
    }
    const page =typedBody.pagination.page;
    const pageSize = typedBody.pagination.pageSize;
    return ok(result.slice(page * pageSize, (page + 1) * pageSize));
  }

  // DELETE
  if (method === 'DELETE') {
    const id = getId(url);
    sightingDocuments = sightingDocuments.filter(u => u.sightingId !== id);
    return ok({});
  }

  return next(req);

  // helper functions

  function ok(body: any) {
    return of(new HttpResponse({status: 200, body})).pipe(delay(300));
  }

  function notFound() {
    return throwError(() => ({
      status: 404,
      error: {message: 'Not found'}
    }));
  }

  function getId(url: string): string {
    return url.split('/').pop();
  }
}
