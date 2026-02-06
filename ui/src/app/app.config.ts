import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {provideHttpClient} from '@angular/common/http';
import {QueryGateway} from './common/query-gateway';
import {CommandGateway} from './common/command-gateway';
import {HandlerRegistry} from './common/handler-registry';

export const appConfig: ApplicationConfig = {
  providers: [provideZoneChangeDetection({ eventCoalescing: true }), provideRouter(routes), provideHttpClient(), QueryGateway, CommandGateway, HandlerRegistry]
};
