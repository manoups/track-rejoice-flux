import {routes} from '../../app.routes';
import {authGuard} from '@edgeflare/ngx-oidc';
import {ContentCreationJourneyComponent} from './content-creation-journey.component';

describe('Content Creation Journey Routing', () => {
  const journeyRoute = routes.find(r => r.path === 'content/new');

  it('should have a route for /content/new', () => {
    expect(journeyRoute).toBeDefined();
  });

  it('should use ContentCreationJourneyComponent', () => {
    expect(journeyRoute!.component).toBe(ContentCreationJourneyComponent);
  });

  it('should have authGuard configured on /content/new (Requirement 8.1, 8.2)', () => {
    expect(journeyRoute!.canActivate).toBeDefined();
    expect(journeyRoute!.canActivate).toContain(authGuard);
  });

  it('should have child routes for pet details and payment', () => {
    expect(journeyRoute!.children).toBeDefined();
    expect(journeyRoute!.children!.length).toBe(2);

    const defaultChild = journeyRoute!.children!.find(r => r.path === '');
    expect(defaultChild).toBeDefined();
    expect(defaultChild!.loadComponent).toBeDefined();

    const paymentChild = journeyRoute!.children!.find(r => r.path === 'payment/:contentId');
    expect(paymentChild).toBeDefined();
    expect(paymentChild!.loadComponent).toBeDefined();
  });
});
