import {Component, DestroyRef, inject, input, OnInit, output, signal} from '@angular/core';
import {combineLatest, debounceTime, distinctUntilChanged, map, Observable, shareReplay, switchMap} from 'rxjs';
import {
  FacetFilter,
  FacetPaginationRequestBody,
  GetFacetStatsResult,
  ValueCountPair
} from '@trackrejoice/typescriptmodels';
import {AsyncPipe, KeyValuePipe, TitleCasePipe} from '@angular/common';
import {toObservable} from '@angular/core/rxjs-interop';
import {HttpClient} from '@angular/common/http';
import {FormControl, FormGroup, FormRecord, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RouterLink} from '@angular/router';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatCheckbox} from '@angular/material/checkbox';
import {tap} from 'rxjs/operators';
import {MatButton} from '@angular/material/button';

type FacetStatsMap = Record<string, ValueCountPair[]>;
type FacetValueGroup = FormRecord<FormControl<boolean>>;          // valueName -> boolean

@Component({
  selector: 'track-rejoice-filter-sidebar',
  imports: [
    AsyncPipe,
    KeyValuePipe,
    ReactiveFormsModule,
    RouterLink,
    MatFormField,
    MatLabel,
    MatInput,
    TitleCasePipe,
    FormsModule,
    MatCheckbox,
    MatButton
  ],
  templateUrl: './filter-sidebar.component.html',
  styleUrl: './filter-sidebar.component.css',
})
export class FilterSidebarComponent implements OnInit {
  private http = inject(HttpClient);
  private destroyRef = inject(DestroyRef);
  getFacetStats: Observable<Map<string, ValueCountPair[]>>;
  facetFields = input.required<GetFacetStatsResult>();
  term = signal('');
  facet = signal<FacetFilter[]>([]);
  ready = signal(false);
  facetChange = output<[string, FacetFilter[]]>();
  filterChange$: Observable<[string, FacetFilter[]]>;
  form = new FormGroup({
    animal: new FormControl<string | null>(''),
  });
  searchForm = new FormGroup({
    search: new FormControl<string>(''),
    facets: new FormRecord<FacetValueGroup>({}),

  });

  constructor() {

    // When term changes: reset + reload first page
    this.filterChange$ = combineLatest([toObservable(this.term).pipe(
      map(v => (v ?? '').trim()),
      debounceTime(300),
      distinctUntilChanged()), toObservable(this.facet)]
    );
  }


  ngOnInit(): void {
    this.ensureFacetControlsFromRecord(this.facetFields().stats ?? {});
    const sub1 = this.searchForm.controls.search.valueChanges.subscribe(val => this.term.set(val))
    const sub2 = this.searchForm.controls.facets.valueChanges.subscribe(facetsValue => {
      // facetsValue: Record<facetName, Record<valueName, boolean>>
      const filters: FacetFilter[] = Object.entries(facetsValue ?? {}).map(([facetName, valueMap]) => {
        const selectedValues = Object.entries(valueMap ?? {})
          .filter(([, checked]) => checked === true)
          .map(([valueName]) => valueName);

        return {facetName, values: selectedValues};
      }).filter(f => (f.values?.length ?? 0) > 0);

      this.facet.set(filters);
    });

    const sub3 = this.filterChange$.subscribe(([filter, facetFilters]) => this.facetChange.emit([filter, facetFilters]));

    this.getFacetStats = this.filterChange$.pipe(map(([filter, facetFilters]) => {
        return {
          facetFilters: facetFilters,
          filter: filter,
          pagination: {page: 0, pageSize: 10},
        } as FacetPaginationRequestBody;
      }),
      switchMap(body =>
        this.http.post<GetFacetStatsResult>('/api/sighting/list/stats', body, {withCredentials: true})),
      map(facetResults => this.mergeStats(facetResults.stats, this.facetFields().stats)),
      tap(stats => this.applyEnabledStateFromStats(stats)),
      shareReplay({bufferSize: 1, refCount: true})
    );


    this.destroyRef.onDestroy(() => {
      sub1.unsubscribe();
      sub2.unsubscribe();
      sub3.unsubscribe();
    });
  }

  facetControl(facetName: string, valueName: string): FormControl<boolean> {
    const facets = this.searchForm.controls.facets;
    const group = facets.controls[facetName];
    const ctrl = group?.controls[valueName];
    return ctrl ?? new FormControl<boolean>(false, { nonNullable: true });
  }

  private ensureFacetControlsFromRecord(record: FacetStatsMap): void {
    for (const [facetName, pairs] of Object.entries(record)) {
      const values = (pairs ?? []).map(p => p.value);
      this.ensureFacetControlsForFacet(facetName, values);
    }
  }

  private ensureFacetControlsForFacet(facetName: string, valueNames: string[]): void {
    const facets = this.searchForm.controls.facets;

    if (!facets.controls[facetName]) {
      facets.addControl(facetName, new FormRecord<FormControl<boolean>>({}));
    }

    const group = facets.controls[facetName];
    for (const valueName of valueNames) {
      if (!group.controls[valueName]) {
        group.addControl(valueName, new FormControl<boolean>(false, { nonNullable: true }));
      }
    }
  }

  private applyEnabledStateFromStats(stats: Map<string, ValueCountPair[]>): void {
    for (const [facetName, pairs] of stats.entries()) {
      // Ensure controls exist
      this.ensureFacetControlsForFacet(facetName, pairs.map(p => p.value));

      const group = this.searchForm.controls.facets.controls[facetName];

      for (const { value: valueName, count } of pairs) {
        const ctrl = group.controls[valueName];

        if (count > 0) {
          if (ctrl.disabled) ctrl.enable({ emitEvent: false });
        } else {
          // optional: clear selection when a value becomes unavailable
          if (ctrl.value === true) ctrl.setValue(false, { emitEvent: false });
          if (ctrl.enabled) ctrl.disable({ emitEvent: false });
        }
      }
    }
  }

  private mergeStats(stats: FacetStatsMap, extraVals: FacetStatsMap): Map<string, ValueCountPair[]> {
    const response = new Map<string, ValueCountPair[]>();

    for (const [facetName, allowedPairs] of Object.entries(extraVals ?? {})) {
      const serverPairs = (stats ?? {})[facetName] ?? [];

      const merged = (allowedPairs ?? []).map(ap => {
        const found = serverPairs.find(sp => sp.value === ap.value);
        return found ?? ({ value: ap.value, count: 0 } as ValueCountPair);
      });

      response.set(facetName, merged);
    }

    return response;
  }
}
