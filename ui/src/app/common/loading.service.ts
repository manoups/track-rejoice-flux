import {Injectable, signal} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {toObservable} from '@angular/core/rxjs-interop';

@Injectable({
  providedIn: 'root',
})
export class LoadingService {
  private loadingSubject = signal<boolean>(false);

  loading$ = toObservable(this.loadingSubject);

  loadingOn() {
    this.loadingSubject.set(true);
  }

  loadingOff() {
    this.loadingSubject.set(false);
  }
}
