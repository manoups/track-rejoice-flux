import {ElementRef, Injectable} from '@angular/core';
import {Gateway} from './gateway';
import {HandlerInvoker} from './handler';
import {of} from 'rxjs';
import {HandlerRegistry} from './handler-registry';

@Injectable()
export class EventGateway extends Gateway {
  protected static invokers: Map<string, HandlerInvoker[]> = new Map();

  constructor(protected override registry: HandlerRegistry) {
    super(EventGateway.invokers, registry);
  }

  publish(eventName: string, payload: any, elementRef?: ElementRef) {
    (this.tryHandleOnDom(eventName, payload, elementRef) || of(null)).subscribe(result => {
      if (result !== false) {
        (this.handlers.get(eventName) || []).forEach(
          h => h.invoker.method.apply(h.instance, [payload]));
      }
    });
  }

  static registerHandlerInvoker(targetName: string, eventName: string, handler: Function) {
    this.addInvoker(this.invokers, targetName, eventName, handler, 'event');
  }
}
