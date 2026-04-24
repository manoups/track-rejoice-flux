import {Component, ElementRef, input, NgZone, OnInit, signal, inject, viewChild} from '@angular/core';
import {DecimalPipe} from '@angular/common';
import {View} from '../../../common/view';
import {Service} from '@trackrejoice/typescriptmodels';
import {MatButton} from '@angular/material/button';
import {environment} from '../../../../environments/environments';

@Component({
  selector: 'track-rejoice-payment-screen',
  imports: [MatButton, DecimalPipe],
  templateUrl: './payment-screen.component.html',
  styleUrl: './payment-screen.component.css',
})
export class PaymentScreenComponent extends View implements OnInit {
  private zone = inject(NgZone);

  contentId = input.required<string>();

  loading = signal(true);
  basicService = signal<Service | null>(null);
  errorMessage = signal<string | null>(null);
  processing = signal(false);
  paymentCancelled = signal<string | null>(null);
  paymentError = signal<string | null>(null);
  paymentSuccess = signal(false);

  paypalButtonContainer = viewChild<ElementRef>('paypalButtonContainer');

  ngOnInit(): void {
    this.fetchBasicService();
  }

  private fetchBasicService(): void {
    this.loading.set(true);
    this.errorMessage.set(null);

    this.sendQuery('/api/services/basic').subscribe({
      next: (service: Service) => {
        this.basicService.set(service);
        this.loading.set(false);
        this.loadPayPalSdk();
      },
      error: () => {
        this.errorMessage.set('Basic service is currently unavailable. Payment cannot be processed.');
        this.loading.set(false);
      },
    });
  }

  private loadPayPalSdk(): void {
    const clientId = environment.paypalClientId;
    if (!clientId) {
      this.errorMessage.set('PayPal is not configured.');
      return;
    }

    const existingScript = document.querySelector('script[src*="paypal.com/sdk/js"]');
    if (existingScript) {
      this.renderPayPalButton();
      return;
    }

    const script = document.createElement('script');
    script.src = `https://www.paypal.com/sdk/js?client-id=${clientId}&currency=EUR`;
    script.async = true;
    script.onload = () => this.zone.run(() => this.renderPayPalButton());
    script.onerror = () => this.zone.run(() => {
      this.errorMessage.set('Failed to load PayPal. Please refresh and try again.');
    });
    document.head.appendChild(script);
  }

  private renderPayPalButton(): void {
    const paypal = (window as any).paypal;
    if (!paypal) {
      this.errorMessage.set('PayPal SDK failed to initialize.');
      return;
    }

    const container = this.paypalButtonContainer()?.nativeElement;
    if (!container) {
      return;
    }

    container.innerHTML = '';

    paypal.Buttons({
      createOrder: (_data: any, actions: any) => {
        return actions.order.create({
          purchase_units: [{
            amount: {
              value: String(this.basicService()?.serviceDetails?.price ?? '0.00'),
              currency_code: 'EUR',
            },
          }],
        });
      },
      onApprove: (data: any) => {
        if (this.processing()) {
          return;
        }
        this.zone.run(() => this.handlePaymentApproval(data.orderID));
      },
      onCancel: () => {
        this.zone.run(() => {
          this.paymentCancelled.set('Payment was cancelled. You can try again when ready.');
          this.paymentError.set(null);
        });
      },
      onError: (err: any) => {
        this.zone.run(() => {
          console.error('PayPal error:', err);
          this.paymentError.set('A payment error occurred. Please try again.');
          this.paymentCancelled.set(null);
        });
      },
    }).render(container);
  }

  private handlePaymentApproval(pspReference: string): void {
    this.processing.set(true);
    this.paymentCancelled.set(null);
    this.paymentError.set(null);

    const service = this.basicService();
    if (!service?.serviceId) {
      this.processing.set(false);
      this.paymentError.set('Service information is missing. Please refresh and try again.');
      return;
    }

    const orderPayload = {
      serviceIds: [service.serviceId],
      updatedAt: new Date().toISOString(),
    };

    this.sendCommand(
      `/orders/${this.contentId()}`,
      orderPayload,
      () => {
        this.sendCommand(
          `/payments/paypal/accepted/${pspReference}`,
          {},
          () => {
            this.processing.set(false);
            this.paymentSuccess.set(true);
          },
          () => {
            this.processing.set(false);
            this.paymentError.set('Payment confirmation failed. Please contact support.');
          },
        );
      },
      () => {
        this.processing.set(false);
        this.paymentError.set('Order creation failed. Please try again.');
      },
    );
  }
}
