// mock-backend.interceptor.ts
import {HttpEvent, HttpHandlerFn, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable, of, throwError} from 'rxjs';
import {delay} from 'rxjs/operators';
import {FacetPaginationRequestBody, SightingDocument} from '@trackrejoice/typescriptmodels';
import {mockSightings, mockUserIds} from './sightings.mock';

let userIds = mockUserIds;

let users: SightingDocument[] = mockSightings;

export function mockBackendInterceptor(
  req: HttpRequest<any>,
  next: HttpHandlerFn
): Observable<HttpEvent<any>> {

  const {method, url, params, body} = req;

  if (!url.startsWith('/api/sighting/list')) {
    return next(req);
  }

  // GET with query params
  if (method === 'GET') {
    let result = [...users];

    const name = params.get('name');
    const email = params.get('email');

    if (name) {
      result = result.filter(u =>
        u.name.toLowerCase().includes(name.toLowerCase())
      );
    }

    if (email) {
      result = result.filter(u =>
        u.email.toLowerCase().includes(email.toLowerCase())
      );
    }

    return ok(result);
  }

  // GET by id
  if (method === 'GET' && url.match(/\/api\/users\/\d+$/)) {
    const id = getId(url);
    const user = users.find(u => u.id === id);
    return user ? ok(user) : notFound();
  }

  // POST
  if (method === 'POST') {
    let typedBody:FacetPaginationRequestBody = req.body;
    let result = [...mockSightings];
    const page =typedBody.pagination.page;
    const pageSize = typedBody.pagination.pageSize;
    return ok(result.slice(page * pageSize, (page + 1) * pageSize));
  }

  // DELETE
  if (method === 'DELETE') {
    const id = getId(url);
    users = users.filter(u => u.id !== id);
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

  function getId(url: string): number {
    return Number(url.split('/').pop());
  }
}
