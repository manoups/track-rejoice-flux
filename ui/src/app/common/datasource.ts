import {DataSource} from '@angular/cdk/collections';
import {BehaviorSubject, Observable} from 'rxjs';

export class TrackRejoiceDataSource<T> extends DataSource<T> {
  private readonly subject = new BehaviorSubject<T[]>([]);

  connect(): Observable<T[]> {
    return this.subject.asObservable();
  }

  disconnect(): void {
    this.subject.complete();
  }

  set(rows: T[]): void {
    this.subject.next(rows);
  }

  append(rows: T[]): void {
    const current = this.subject.value;
    this.subject.next(current.concat(rows ?? []));
  }

  get length(): number {
    return this.subject.value.length;
  }
}
