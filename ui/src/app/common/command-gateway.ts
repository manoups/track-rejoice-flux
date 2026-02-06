import {RequestGateway, RequestOptions} from './request-gateway';
import {HttpClient} from '@angular/common/http';
import {ElementRef, Injectable} from '@angular/core';
import {HandlerInvoker, HandlerOptions} from './handler';
import {Observable, take} from "rxjs";
import {tap} from 'rxjs/operators';
import {HandlerRegistry} from './handler-registry';

@Injectable()
export class CommandGateway extends RequestGateway {
  protected static invokers: Map<string, HandlerInvoker[]> = new Map();

  constructor(protected override registry: HandlerRegistry, http: HttpClient) {
    super(CommandGateway.invokers, registry, http, "post");
  }

  override send(type: string, payload: any, options: CommandOptions = {}, elementRef?: ElementRef<Element>): Observable<any> {
    let observable = super.send(type, payload, options, elementRef).pipe(take(1));
    if (options.eventOnSuccess) {
      observable = observable.pipe((tap(value => {
        const event = new CustomEvent<any>("commandSuccess", {
          detail: value,
          bubbles: true,
          cancelable: true
        });
        if (elementRef?.nativeElement) {
          elementRef.nativeElement.dispatchEvent(event);
        } else {
          document.dispatchEvent(event);
        }
      })));
    }
    return observable;
  }

  sendAndForget(type: string, payload: any, options: CommandOptions = {}, elementRef?: ElementRef<Element>): void {
    this.send(type, payload, options, elementRef).subscribe();
  }

  static registerHandlerInvoker(target: any, type: string, handler: Function, options?: HandlerOptions) {
    this.addInvoker(this.invokers, target, type, handler, 'command', options);
  }
}

export interface CommandOptions extends RequestOptions {
  eventOnSuccess? : boolean
}
