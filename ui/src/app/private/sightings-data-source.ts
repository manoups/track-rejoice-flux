import {Component, DestroyRef, inject, OnInit} from '@angular/core';
import {DataSource} from '@angular/cdk/collections';
import {BehaviorSubject, Observable} from 'rxjs';
import {SightingDocument} from '@trackrejoice/typescriptmodels';

class SightingsDataSource extends DataSource<SightingDocument> {
  private readonly subject = new BehaviorSubject<SightingDocument[]>([]);

  connect(): Observable<SightingDocument[]> {
    return this.subject.asObservable();
  }

  disconnect(): void {
    this.subject.complete();
  }

  set(rows: SightingDocument[]): void {
    this.subject.next(rows);
  }

  append(rows: SightingDocument[]): void {
    const current = this.subject.value;
    this.subject.next(current.concat(rows ?? []));
  }

  get length(): number {
    return this.subject.value.length;
  }
}

@Component({
  selector: 'track-rejoice-sightings',
  // imports: include MatTable bits + CdkVirtualScrollViewport + DatePipe + MatToolbar, etc.
  templateUrl: './sightings.component.html',
  styleUrl: './sightings.component.css',
})
export class SightingsComponent implements OnInit {
  private destroyRef = inject(DestroyRef);

  rowHeight = 48;
  displayedColumns: string[] = ['id', 'ownerId', 'timestamp'];

  dataSource = new SightingsDataSource();

  trackBySightingId = (_: number, row: SightingDocument) => (row as any).sightingId;

  // Your existing loading/paging logic can call:
  // this.dataSource.set([]) on reset
  // this.dataSource.append(rows) on next page

  ngOnInit(): void {
    // when you load a page successfully:
    // this.dataSource.append(rows);
  }

  onScrollIndexChange(index: number): void {
    // Use your existing “near end” logic, but base it on dataSource.length:
    const buffer = 8;
    if (index + buffer >= this.dataSource.length) {
      // trigger next page load
    }
  }
}
