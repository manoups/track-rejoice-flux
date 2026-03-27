import {ApplicationConfig, inject, provideAppInitializer, provideZoneChangeDetection} from '@angular/core';
import {provideRouter, Router, withComponentInputBinding, withRouterConfig} from '@angular/router';
import { UserManagerSettings } from 'oidc-client-ts';
import { routes } from './app.routes';
import {
  HTTP_INTERCEPTORS,
  provideHttpClient,
  withFetch,
  withInterceptors,
  withInterceptorsFromDi
} from '@angular/common/http';
import {QueryGateway} from './common/query-gateway';
import {CommandGateway} from './common/command-gateway';
import {HandlerRegistry} from './common/handler-registry';
import {LoadingInterceptor} from './common/loading-interceptor';
import {mockBackendInterceptor} from './interceptors/sighting.interceptor';
import {environment} from '../environments/environments';
import {mockStatsInterceptor} from './interceptors/sighting.stats.interceptor';
import {mockSightingCreateInterceptor} from './interceptors/sighting.create.interceptor';
import {mockBackendDetailsInterceptor} from './interceptors/sighting.details.interceptor';
import {mockWeightedAssociationsInterceptor} from './interceptors/weighted.association.interceptor';
import { ZITADEL_SCOPES } from './config/scopes';
import {AuthService, authzTokenInterceptor, OIDC_CONFIG_TOKEN} from '@edgeflare/ngx-oidc';

function hasAuthParams(location = window.location): boolean {
  let searchParams = new URLSearchParams(location.search);
  if ((searchParams.get('code') || searchParams.get('error')) && searchParams.get('state')) {
    return true;
  }
  searchParams = new URLSearchParams(location.hash.replace('#', '?'));
  return !!((searchParams.get('code') || searchParams.get('error')) && searchParams.get('state'));
}

const oidcConfig: UserManagerSettings = {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  authority: environment.auth.domain,
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  client_id: environment.auth.clientId,
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  redirect_uri: `${environment.auth.redirectBaseUri}/welcome`,
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  post_logout_redirect_uri: environment.auth.redirectBaseUri,
  scope: ZITADEL_SCOPES,
  loadUserInfo: true,
  automaticSilentRenew: false,
};

function initializeAuth() {
  const authService = inject(AuthService);
  const router = inject(Router);
  console.log('ZITADEL domain:', environment.auth.domain);
  return (async () => {
    try {
      // Only handle callback if we have auth params
      if (hasAuthParams()) {
        await authService.signinCallback();
        // Clean the URL before navigating
        window.history.replaceState({}, document.title, window.location.pathname);
        await router.navigate(['/profile']);
        return; // Exit early after handling callback
      }

      // Only handle logout callback on the specific path
      if (window.location.pathname === '/auth/logout/callback') {
        await authService.signoutCallback();
        await router.navigate(['/']);
        return; // Exit early after handling logout
      }

      // For all other pages, just load the existing user (don't try to refresh)
      // The AuthService will handle token refresh automatically when needed
    } catch (error) {
      console.error('Auth initialization error:', error);
      // Only navigate to error page if we were actually trying to authenticate
      if (hasAuthParams() || window.location.pathname === '/auth/logout/callback') {
        await router.navigate(['/auth/error']);
      }
    }
  })();
}

export const appConfig: ApplicationConfig = {
  providers: [provideZoneChangeDetection({ eventCoalescing: true }),
    {
      provide: OIDC_CONFIG_TOKEN,
      useValue: oidcConfig,
    },
    {
      provide: AuthService,
      useClass: AuthService,
    }, provideRouter(routes, withComponentInputBinding(), withRouterConfig({paramsInheritanceStrategy: 'always'})), provideHttpClient(environment.mock
    ? withInterceptors([mockBackendInterceptor, mockStatsInterceptor, mockSightingCreateInterceptor, mockBackendDetailsInterceptor, mockWeightedAssociationsInterceptor])
    : withInterceptorsFromDi(), withFetch(), withInterceptors([authzTokenInterceptor])), provideAppInitializer(initializeAuth), QueryGateway, CommandGateway, HandlerRegistry, {provide: HTTP_INTERCEPTORS, useClass: LoadingInterceptor, multi: true}]
};
