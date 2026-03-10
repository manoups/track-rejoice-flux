import {AfterViewInit, Component, DestroyRef, effect, inject, input, OnInit, signal, ViewChild} from '@angular/core';
import {View} from '../../common/view';
import {FacetFilter, FacetPaginationRequestBody, SightingDocument} from '@trackrejoice/typescriptmodels';
import {FormsModule} from '@angular/forms';
import {CdkFixedSizeVirtualScroll, CdkVirtualForOf, CdkVirtualScrollViewport} from '@angular/cdk/scrolling';
import {DatePipe} from '@angular/common';
import {fromEvent, map, merge, Observable, switchMap, withLatestFrom} from 'rxjs';
import {tap} from 'rxjs/operators';
import {SelectedItemComponent} from '../selected-item/selected-item.component';
import {SightingListingService} from '../sighting-listing.service';
import {takeUntilDestroyed, toObservable} from '@angular/core/rxjs-interop';

@Component({
  selector: 'track-rejoice-sightings',
  imports: [
    FormsModule,
    CdkVirtualScrollViewport,
    CdkFixedSizeVirtualScroll,
    DatePipe,
    SelectedItemComponent,
    CdkVirtualForOf
  ],
  templateUrl: './sightings.component.html',
  styleUrl: './sightings.component.css',
  standalone: true
})
export class SightingsComponent extends View implements OnInit, AfterViewInit {
  private destroyRef = inject(DestroyRef);
  private sightingListingService = inject(SightingListingService);

  dataSource = this.sightingListingService.dataSource;
  sightings = this.sightingListingService.sightings;
  // Virtual/infinite state
  loading = this.sightingListingService.loading.asReadonly()
  done = this.sightingListingService.done;

  filterState = input.required<[string, FacetFilter[]]>();
  filterUpdate$ = toObservable(this.filterState);
  @ViewChild(CdkVirtualScrollViewport)
  viewport!: CdkVirtualScrollViewport;

  private pageSize = 20;
  // readonly page = this.sightingListingService.page;
  rowHeight = 48;

  protected displayedColumns = ['id', 'ownerId', 'timestamp', 'type', 'removeAfterMatching'];
  clickedRow = signal<SightingDocument>(null);

  constructor() {
    super();
    effect(() => {
      /*The following row tells Angular: "This effect depends on the sightings signal."
         The assignment to rows is just a convenient way to make the read explicit*/
      const rows = this.sightings();

      queueMicrotask(() => {
        this.viewport?.checkViewportSize();
      });
    });
  }


  ngOnInit(): void {
    const filterRequests$: Observable<FacetPaginationRequestBody> = this.filterUpdate$
      .pipe(tap(_ => this.sightingListingService.resetListing()),
        map(([filter, facetFilters]) => this.toRequestBody(filter, facetFilters, 0)));

    const pageRequests$: Observable<FacetPaginationRequestBody> = this.sightingListingService.pageSubject.pipe(
      withLatestFrom(this.filterUpdate$),
      map(([page, [filter, facetFilters]]) => this.toRequestBody(filter, facetFilters, page)))

    merge(filterRequests$, pageRequests$)
      .pipe(switchMap((body) => this.sightingListingService.loadNextPage(body)),
        takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (rows) => {
          this.sightings.update(sightings => sightings.concat(rows))
          this.dataSource.append(rows);
          this.sightingListingService.page += 1;
          if ((rows).length < this.pageSize) {
            this.done.set(true);
          }
        },
        error: () => {
          // optional: show error state here; loading already reset by finalize()
        }
      });
  }

  ngAfterViewInit() {
    const resizeObserver = new ResizeObserver(() => {
      this.viewport.checkViewportSize();
    });

    resizeObserver.observe(this.viewport.elementRef.nativeElement);
    this.destroyRef.onDestroy(() => {
      resizeObserver.disconnect()
    })

    this.filterUpdate$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.viewport.scrollToIndex(0);
        this.viewport.checkViewportSize();
      })

    fromEvent(window, 'resize')
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.viewport.checkViewportSize();
      });
  }

  onScrollIndexChange(index: number): void {
    this.sightingListingService.onScrollIndexChange(index);
  }

  private toRequestBody = (filter: string, facetFilters: FacetFilter[], page: number) => {
    return {filter, facetFilters, pagination: {page, pageSize: this.pageSize}};
  }

  trackBySightingId = (_index: number, sighting: SightingDocument) => (sighting as any).sightingId;


}
