import {FacetPaginationRequestBody, GetFacetStatsResult} from '@trackrejoice/typescriptmodels';
import {HttpEvent, HttpHandlerFn, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable, of, throwError} from 'rxjs';
import {delay} from 'rxjs/operators';

// let sightingDocuments: SightingDocument[] = mockSightings;

export function mockStatsInterceptor(
  req: HttpRequest<any>,
  next: HttpHandlerFn
): Observable<HttpEvent<any>> {

  const {method, url, params, body} = req;

  if (!url.startsWith('/api/sighting') || !url.endsWith('/stats')) {
    return next(req);
  }

  // POST
  if (method === 'POST') {
    let typedBody: FacetPaginationRequestBody = req.body;
    // let result = [...mockSightings];
    // const page = typedBody.pagination.page;
    // const pageSize = typedBody.pagination.pageSize;
    const result = {'@type': 'GetFacetStatsResult', stats: {"type": [{"value": "cat", "count": 12}]}} as GetFacetStatsResult;
    return ok(result);
  }

  return next(req);

  function ok(body: any) {
    return of(new HttpResponse({status: 200, body})).pipe(delay(300));
  }

  function notFound() {
    return throwError(() => ({
      status: 404,
      error: {message: 'Not found'}
    }));
  }
}
