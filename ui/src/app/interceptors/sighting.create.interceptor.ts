import {CreateSightingDTO} from '@trackrejoice/typescriptmodels';
import {HttpEvent, HttpHandlerFn, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable, of, throwError} from 'rxjs';
import {delay} from 'rxjs/operators';

export function mockSightingCreateInterceptor(
  req: HttpRequest<any>,
  next: HttpHandlerFn
): Observable<HttpEvent<any>> {

  const {method, url} = req;

  if (!url.startsWith('/api') || !url.endsWith('sighting')) {
    return next(req);
  }

  // POST
  if (method === 'POST') {
    let typedBody: CreateSightingDTO = req.body;
    if(!typedBody.subtype || !typedBody.sightingDetails || !typedBody.sightingDetails.lat || !typedBody.sightingDetails.lng){
      return notFound();
    }
    // let result = [...mockSightings];
    // const page = typedBody.pagination.page;
    // const pageSize = typedBody.pagination.pageSize;
    return ok({});
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
