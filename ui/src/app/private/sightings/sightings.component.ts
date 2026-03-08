import {AfterViewInit, Component, DestroyRef, effect, inject, Input, OnInit, signal, ViewChild} from '@angular/core';
import {View} from '../../common/view';
import {FacetFilter, FacetPaginationRequestBody, SightingDocument} from '@trackrejoice/typescriptmodels';
import {FormsModule} from '@angular/forms';
import {CdkFixedSizeVirtualScroll, CdkVirtualForOf, CdkVirtualScrollViewport} from '@angular/cdk/scrolling';
import {DatePipe} from '@angular/common';
import {map, merge, Observable, switchMap, withLatestFrom} from 'rxjs';
import {tap} from 'rxjs/operators';
import {SelectedItemComponent} from '../selected-item/selected-item.component';
import {SightingListingService} from '../sighting-listing.service';

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
  @Input({required: true}) filterUpdate$: Observable<[string, FacetFilter[]]>;
  @ViewChild(CdkVirtualScrollViewport)
  viewport!: CdkVirtualScrollViewport;
  private pageSize = 20;
  readonly page = this.sightingListingService.page;
  rowHeight = 48;

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
    const filterObservable$: Observable<FacetPaginationRequestBody> = this.filterUpdate$
      .pipe(tap(_ => this.sightingListingService.resetListing()),
        map(([filter, facetFilters]) => {
          return {filter: filter, facetFilters: facetFilters, pagination: {page: 0, pageSize: this.pageSize}}
        }));

    const pageObservable$: Observable<FacetPaginationRequestBody> = this.sightingListingService.pageSubject.pipe(
      withLatestFrom(this.filterUpdate$),
      map(([page, [filter, facetFilters]]) => {
        return {filter: filter, facetFilters: facetFilters, pagination: {page: page, pageSize: this.pageSize}}
      }))

    const subscribe = merge(filterObservable$, pageObservable$).pipe(switchMap((body) => this.sightingListingService.loadNextPage(body)))
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

    this.destroyRef.onDestroy(() => {
      subscribe.unsubscribe();
    });
  }

  ngAfterViewInit() {
    const resizeObserver = new ResizeObserver(() => {
      this.viewport.checkViewportSize();
    });

    resizeObserver.observe(this.viewport.elementRef.nativeElement);

    const viewSubscription = this.filterUpdate$.subscribe(() => {
      this.viewport.scrollToIndex(0);
      this.viewport.checkViewportSize();
    })

    window.addEventListener('resize', () => {
      this.viewport.checkViewportSize();
    });

    this.destroyRef.onDestroy(() => {
      viewSubscription.unsubscribe();
    })
  }

  onScrollIndexChange(index: number): void {
    this.sightingListingService.onScrollIndexChange(index);
  }


  trackBySightingId = (_index: number, sighting: SightingDocument) => (sighting as any).sightingId;

  protected displayedColumns = ['id', 'ownerId', 'timestamp', 'type', 'removeAfterMatching'];
  clickedRow = signal<SightingDocument>(null);
}
