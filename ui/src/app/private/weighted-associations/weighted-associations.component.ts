import {Component, DestroyRef, inject, input, OnInit, signal} from '@angular/core';
import {CdkFixedSizeVirtualScroll, CdkVirtualScrollViewport} from '@angular/cdk/scrolling';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable
} from '@angular/material/table';
import {SelectedItemComponent} from '../selected-item/selected-item.component';
import {TrackRejoiceDataSource} from '../../common/datasource';
import {FacetPaginationRequestBody, WeightedAssociationState} from '@trackrejoice/typescriptmodels';
import {BehaviorSubject, finalize, Observable, of, switchMap} from 'rxjs';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'track-rejoice-weighted-associations',
  imports: [
    CdkFixedSizeVirtualScroll,
    CdkVirtualScrollViewport,
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
    MatTable,
    SelectedItemComponent,
    MatHeaderCellDef
  ],
  templateUrl: './weighted-associations.component.html',
  styleUrl: './weighted-associations.component.css',
})
export class WeightedAssociationsComponent implements OnInit {
  private http = inject(HttpClient);
  private destroyRef = inject(DestroyRef);
  sightingId = input.required<string>();
  dataSource = new TrackRejoiceDataSource<WeightedAssociationState>();
  trackByWeightedAssociationId = (_index: number, sighting: WeightedAssociationState) => (sighting as any).weightedAssociationId;
  protected displayedColumns = ['weightedAssociationId', 'distance', 'score', 'status'];
  clickedRow = signal<WeightedAssociationState>(null);
  rowHeight = 48;
  loading = signal<boolean>(false);
  done = signal<boolean>(false);
  weightedAssociationStates = signal<WeightedAssociationState[]>([]);
  private pageSize = 20;
  pageSubject = new BehaviorSubject<number>(0);
  page = 0;

  ngOnInit(): void {
    const subscribe = this.pageSubject.pipe(switchMap(page => this.loadNextPage({
      facetFilters: [],
      filter: this.sightingId(),
      pagination: {page: page, pageSize: this.pageSize}
    }))).subscribe(
      (rows) => {
        this.weightedAssociationStates.update(weightedAssociationStates => weightedAssociationStates.concat(rows))
        this.dataSource.append(rows);
        this.page += 1;
        if ((rows).length < this.pageSize) {
          this.done.set(true);
        }
      });

    this.destroyRef.onDestroy(() => {
      subscribe.unsubscribe();
    });
  }

  private loadNextPage(body: FacetPaginationRequestBody): Observable<WeightedAssociationState[]> {
    if (this.loading() || this.done()) return of([]);

    this.loading.set(true);

    return this.http.post<WeightedAssociationState[]>('/api/weighted/association/list', body, {withCredentials: true}).pipe(
      finalize(() => {
        this.loading.set(false);
      })
    )
  }

  onScrollIndexChange(index: number): void {
    // Load next page when user gets close to the end
    if (this.loading() || this.done()) return;
    if (index + this.pageSize >= this.weightedAssociationStates().length) {
      this.pageSubject.next(this.page);
    }
  }

}
