import * as fc from 'fast-check';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {HttpClient} from '@angular/common/http';
import {of, throwError} from 'rxjs';
import {PaymentScreenComponent} from './payment-screen.component';
import {QueryGateway} from '../../../common/query-gateway';
import {CommandGateway} from '../../../common/command-gateway';
import {HandlerRegistry} from '../../../common/handler-registry';
import {ComponentRef} from '@angular/core';
import {Service, ServiceId} from '@trackrejoice/typescriptmodels';

describe('PaymentScreenComponent', () => {

  // Feature: pet-content-creation-journey, Property 5: Order creation sends correct Basic_Service ServiceId
  // Validates: Requirements 6.2
  describe('Property 5: Order creation sends correct Basic_Service ServiceId', () => {
    let fixture: ComponentFixture<PaymentScreenComponent>;
    let component: PaymentScreenComponent;
    let componentRef: ComponentRef<PaymentScreenComponent>;
    let httpSpy: jasmine.SpyObj<HttpClient>;

    beforeEach(() => {
      httpSpy = jasmine.createSpyObj('HttpClient', ['get', 'post']);
      // Default: GET for basic service returns empty (overridden per test iteration)
      httpSpy.get.and.returnValue(of(null));

      TestBed.configureTestingModule({
        imports: [PaymentScreenComponent],
        providers: [
          {provide: HttpClient, useValue: httpSpy},
          QueryGateway,
          CommandGateway,
          HandlerRegistry,
        ],
      });

      fixture = TestBed.createComponent(PaymentScreenComponent);
      component = fixture.componentInstance;
      componentRef = fixture.componentRef;
    });

    afterEach(() => {
      fixture.destroy();
    });

    // Arbitrary for ContentId: non-empty alphanumeric strings with hyphens (UUID-like)
    const contentIdArb = fc.stringMatching(/^[a-zA-Z0-9-]{1,64}$/);

    // Arbitrary for ServiceId: object with a type field containing a random non-empty string
    const serviceIdArb = fc.record({
      type: fc.string({minLength: 1, maxLength: 50}).filter(s => s.trim().length > 0),
    });

    it('should POST to /orders/{contentId} with the basic service ServiceId in the serviceIds list', () => {
      fc.assert(
        fc.property(
          contentIdArb,
          serviceIdArb,
          (contentId: string, serviceId: ServiceId) => {
            // Reset spy call history
            httpSpy.post.calls.reset();

            // The first POST creates the order, the second confirms payment — both succeed
            httpSpy.post.and.returnValue(of({}));

            // Set the contentId input
            componentRef.setInput('contentId', contentId);

            // Set the basicService signal with a Service containing the generated ServiceId
            const service: Service = {
              serviceId: serviceId,
              basic: true,
              online: true,
              serviceDetails: {name: 'Basic Service', price: 9.99},
            };
            component.basicService.set(service);

            // Trigger the payment approval flow by calling handlePaymentApproval indirectly
            // The method is private, so we access it via bracket notation
            (component as any).handlePaymentApproval('fake-psp-ref');

            // Assert: at least one POST was made (the order creation)
            expect(httpSpy.post).toHaveBeenCalled();

            // The first POST call should be the order creation
            const [url, payload] = httpSpy.post.calls.first().args;

            // Assert: correct URL with contentId
            expect(url).toBe(`/orders/${contentId}`);

            // Assert: payload contains the basic service's ServiceId in the serviceIds list
            expect(payload.serviceIds).toBeDefined();
            expect(payload.serviceIds.length).toBe(1);
            expect(payload.serviceIds[0]).toEqual(serviceId);
          }
        ),
        {numRuns: 100}
      );
    });
  });

  // Feature: pet-content-creation-journey, Unit Tests: Payment screen rendering and behavior
  // Validates: Requirements 5.2, 6.1, 6.4, 6.5, 7.3
  describe('Unit Tests: Payment screen rendering and behavior', () => {
    let fixture: ComponentFixture<PaymentScreenComponent>;
    let component: PaymentScreenComponent;
    let componentRef: ComponentRef<PaymentScreenComponent>;
    let httpSpy: jasmine.SpyObj<HttpClient>;

    beforeEach(() => {
      httpSpy = jasmine.createSpyObj('HttpClient', ['get', 'post']);
      httpSpy.get.and.returnValue(of(null));

      TestBed.configureTestingModule({
        imports: [PaymentScreenComponent],
        providers: [
          {provide: HttpClient, useValue: httpSpy},
          QueryGateway,
          CommandGateway,
          HandlerRegistry,
        ],
      });

      fixture = TestBed.createComponent(PaymentScreenComponent);
      component = fixture.componentInstance;
      componentRef = fixture.componentRef;
      componentRef.setInput('contentId', 'test-content-123');
    });

    afterEach(() => {
      fixture.destroy();
    });

    it('should display the paypal-button-container when basic service is available', () => {
      const service: Service = {
        serviceId: {type: 'svc-1'},
        basic: true,
        online: true,
        serviceDetails: {name: 'Basic Service', price: 9.99},
      };
      httpSpy.get.and.returnValue(of(service));

      fixture.detectChanges();

      const el: HTMLElement = fixture.nativeElement;
      const container = el.querySelector('#paypal-button-container');
      expect(container).toBeTruthy();
    });

    it('should display content summary with contentId', () => {
      httpSpy.get.and.returnValue(of({serviceId: {type: 'svc-1'}, basic: true, online: true, serviceDetails: {name: 'Basic', price: 5}}));

      fixture.detectChanges();

      const el: HTMLElement = fixture.nativeElement;
      const summary = el.querySelector('.content-summary');
      expect(summary).toBeTruthy();
      expect(summary.textContent).toContain('test-content-123');
    });

    it('should show loading state during payment processing', () => {
      const service: Service = {
        serviceId: {type: 'svc-1'},
        basic: true,
        online: true,
        serviceDetails: {name: 'Basic Service', price: 9.99},
      };
      httpSpy.get.and.returnValue(of(service));

      fixture.detectChanges();

      // Set processing state
      component.processing.set(true);
      fixture.detectChanges();

      const el: HTMLElement = fixture.nativeElement;
      const processingIndicator = el.querySelector('.loading-indicator');
      expect(processingIndicator).toBeTruthy();
      expect(processingIndicator.textContent).toContain('Processing payment');

      // PayPal button container should be hidden during processing
      const container = el.querySelector('.paypal-button-container');
      expect(container.classList.contains('hidden')).toBeTrue();
    });

    it('should show error/retry message on payment rejection', () => {
      const service: Service = {
        serviceId: {type: 'svc-1'},
        basic: true,
        online: true,
        serviceDetails: {name: 'Basic Service', price: 9.99},
      };
      httpSpy.get.and.returnValue(of(service));

      fixture.detectChanges();

      // Simulate payment cancellation
      component.paymentCancelled.set('Payment was cancelled. You can try again when ready.');
      fixture.detectChanges();

      const el: HTMLElement = fixture.nativeElement;
      const cancelledMsg = el.querySelector('.payment-cancelled');
      expect(cancelledMsg).toBeTruthy();
      expect(cancelledMsg.textContent).toContain('Payment was cancelled');

      // PayPal button container should still be present for retry
      const container = el.querySelector('#paypal-button-container');
      expect(container).toBeTruthy();
    });

    it('should show payment error message and allow retry', () => {
      const service: Service = {
        serviceId: {type: 'svc-1'},
        basic: true,
        online: true,
        serviceDetails: {name: 'Basic Service', price: 9.99},
      };
      httpSpy.get.and.returnValue(of(service));

      fixture.detectChanges();

      // Simulate payment error
      component.paymentError.set('A payment error occurred. Please try again.');
      fixture.detectChanges();

      const el: HTMLElement = fixture.nativeElement;
      const errorMsgs = Array.from(el.querySelectorAll('.control-error'))
        .filter(e => e.textContent.includes('payment error'));
      expect(errorMsgs.length).toBe(1);

      // PayPal button container should still be present for retry
      const container = el.querySelector('#paypal-button-container');
      expect(container).toBeTruthy();
    });

    it('should show success confirmation after payment', () => {
      httpSpy.get.and.returnValue(of({serviceId: {type: 'svc-1'}, basic: true, online: true, serviceDetails: {name: 'Basic', price: 5}}));

      fixture.detectChanges();

      // Simulate successful payment
      component.paymentSuccess.set(true);
      fixture.detectChanges();

      const el: HTMLElement = fixture.nativeElement;
      const successSection = el.querySelector('.success-section');
      expect(successSection).toBeTruthy();

      const successMsg = el.querySelector('.success-message');
      expect(successMsg).toBeTruthy();
      expect(successMsg.textContent).toContain('now online');

      // PayPal section should not be visible after success
      const paypalSection = el.querySelector('.paypal-section');
      expect(paypalSection).toBeFalsy();
    });

    it('should show error message when basic service is not found', () => {
      httpSpy.get.and.returnValue(throwError(() => new Error('Not found')));

      fixture.detectChanges();

      const el: HTMLElement = fixture.nativeElement;
      const errorMsgs = Array.from(el.querySelectorAll('.control-error'))
        .filter(e => e.textContent.includes('unavailable'));
      expect(errorMsgs.length).toBe(1);

      // Should show disabled PayPal button instead of the container
      const disabledButton = el.querySelector('button[disabled]');
      expect(disabledButton).toBeTruthy();

      // PayPal button container should not be present
      const container = el.querySelector('#paypal-button-container');
      expect(container).toBeFalsy();
    });
  });
});
