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

  update(predicate: (item: T) => boolean, replacement: T): void {
    const current = this.subject.value;
    this.subject.next(current.map(item => predicate(item) ? replacement : item));
  }

  remove(predicate: (item: T) => boolean): void {
    const current = this.subject.value;
    this.subject.next(current.filter(item => !predicate(item)));
  }

  get length(): number {
    return this.subject.value.length;
  }

  get data(): T[] {
    return this.subject.value;
  }
}
