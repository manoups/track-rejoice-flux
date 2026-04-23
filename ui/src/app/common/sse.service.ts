import {Injectable} from '@angular/core';
import {Observable, Subscriber} from 'rxjs';
import {SseEvent} from './sse-event.model';

/**
 * Calculates exponential backoff delay in seconds.
 * Formula: min(2^(n-1), 30) where n >= 1.
 */
export function calculateBackoffDelay(failureCount: number): number {
  return Math.min(Math.pow(2, failureCount - 1), 30);
}

@Injectable({
  providedIn: 'root'
})
export class SseService {

  connect(url: string): Observable<SseEvent> {
    return new Observable<SseEvent>((subscriber: Subscriber<SseEvent>) => {
      let eventSource: EventSource | null = null;
      let lastEventId: string | null = null;
      let failureCount = 0;
      let reconnectTimer: ReturnType<typeof setTimeout> | null = null;
      let closed = false;

      const openConnection = () => {
        if (closed) {
          return;
        }

        let connectUrl = url;
        if (lastEventId) {
          const separator = url.includes('?') ? '&' : '?';
          connectUrl = `${url}${separator}lastEventId=${encodeURIComponent(lastEventId)}`;
        }

        eventSource = new EventSource(connectUrl, {withCredentials: true});

        eventSource.addEventListener('snapshot', (event: MessageEvent) => {
          if (closed) return;
          trackEventId(event);
          failureCount = 0;
          try {
            const records: SseEvent[] = JSON.parse(event.data);
            records.forEach(record => subscriber.next(record));
          } catch (e) {
            console.error('Error parsing snapshot event', e);
          }
        });

        eventSource.addEventListener('update', (event: MessageEvent) => {
          if (closed) return;
          trackEventId(event);
          failureCount = 0;
          try {
            const record: SseEvent = JSON.parse(event.data);
            subscriber.next(record);
          } catch (e) {
            console.error('Error parsing update event', e);
          }
        });

        eventSource.onerror = () => {
          if (closed) return;
          closeEventSource();
          failureCount++;

          subscriber.next({changeType: 'DISCONNECTED' as any, payload: null});

          const delaySec = calculateBackoffDelay(failureCount);
          console.warn(`SSE connection error. Reconnecting in ${delaySec}s (attempt ${failureCount})...`);
          reconnectTimer = setTimeout(() => {
            reconnectTimer = null;
            openConnection();
          }, delaySec * 1000);
        };
      };

      const trackEventId = (event: MessageEvent) => {
        if (event.lastEventId) {
          lastEventId = event.lastEventId;
        }
      };

      const closeEventSource = () => {
        if (eventSource) {
          eventSource.close();
          eventSource = null;
        }
      };

      openConnection();

      return () => {
        closed = true;
        if (reconnectTimer) {
          clearTimeout(reconnectTimer);
          reconnectTimer = null;
        }
        closeEventSource();
      };
    });
  }
}
