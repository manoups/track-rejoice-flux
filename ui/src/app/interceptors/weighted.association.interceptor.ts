import {FacetPaginationRequestBody, SightingDocument} from '@trackrejoice/typescriptmodels';
import {HttpEvent, HttpHandlerFn, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable, of, throwError} from 'rxjs';
import {delay} from 'rxjs/operators';
import {weightedAssociations} from './weighted.associations.mock';

let sightingDocuments: SightingDocument[] = weightedAssociations;

export function mockWeightedAssociationsInterceptor(
  req: HttpRequest<any>,
  next: HttpHandlerFn
): Observable<HttpEvent<any>> {

  const {method, url, params, body} = req;

  const path = url.split('?')[0];

  // Match exactly: /api/sighting/<id>   (but NOT /api/sighting/list)
  // If you want to enforce UUID shape, replace ([^/]+) with the UUID regex below.
  const detailsMatch = path.match(/^\/api\/weighted\/association\/([^/]+)$/);

  if (!detailsMatch) {
    return next(req);
  }

  const id = decodeURIComponent(detailsMatch[1]);

  // Let the list interceptor handle /api/sighting/list
  // if (id === 'list') {
  //   return next(req);
  // }

  // POST
  if (method === 'POST') {
    let typedBody:FacetPaginationRequestBody = req.body;
    let result = [...weightedAssociations.filter(it => it.sightingId == typedBody.filter)];
    const page =typedBody.pagination.page;
    const pageSize = typedBody.pagination.pageSize;
    return ok(result.slice(page * pageSize, (page + 1) * pageSize));
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
