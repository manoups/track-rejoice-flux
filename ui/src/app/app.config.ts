import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {HTTP_INTERCEPTORS, provideHttpClient} from '@angular/common/http';
import {QueryGateway} from './common/query-gateway';
import {CommandGateway} from './common/command-gateway';
import {HandlerRegistry} from './common/handler-registry';
import {LoadingInterceptor} from './common/loading-interceptor';

export const appConfig: ApplicationConfig = {
  providers: [provideZoneChangeDetection({ eventCoalescing: true }), provideRouter(routes), provideHttpClient(), QueryGateway, CommandGateway, HandlerRegistry, {provide: HTTP_INTERCEPTORS, useClass: LoadingInterceptor, multi: true}]
};
