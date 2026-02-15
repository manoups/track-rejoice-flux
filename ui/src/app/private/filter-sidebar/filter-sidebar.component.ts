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
import {tap} from 'rxjs/operators';
import {MatButton} from '@angular/material/button';
import {MatListOption, MatSelectionList} from '@angular/material/list';

type FacetStatsMap = Record<string, ValueCountPair[]>;

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
    MatButton,
    MatSelectionList,
    MatListOption
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
  facetChange = output<[string, FacetFilter[]]>();
  filterChange$: Observable<[string, FacetFilter[]]>;
  searchForm = new FormGroup({
    search: new FormControl<string>(''),
    facets: new FormRecord<FormControl<string[]>>({}),

  });

  constructor() {

    // When term changes: reset + reload first page
    this.filterChange$ = combineLatest([toObservable(this.term).pipe(
      map(v => (v ?? '').trim()),
      debounceTime(300),
      distinctUntilChanged()), toObservable(this.facet)]
    );
  }

  private sanitizeSelectionsAgainstFacetFields(): void {
    const allowedByFacet: FacetStatsMap = this.facetFields().stats ?? {};
    const facetsCtrl = this.searchForm.controls.facets;

    for (const [facetName, ctrl] of Object.entries(facetsCtrl.controls)) {
      const allowedSet = new Set((allowedByFacet[facetName] ?? []).map(p => p.value));

      const current = ctrl.value ?? [];
      const next = current.filter(v => allowedSet.has(v));

      if (!arrayEqual(current, next)) {
        ctrl.setValue(next, { emitEvent: false });
      }
    }
  }

  ngOnInit(): void {
    this.ensureFacetControlsFromRecord(this.facetFields().stats ?? {});
    const sub1 = this.searchForm.controls.search.valueChanges.subscribe(val => this.term.set(val))
    const sub2 = this.searchForm.controls.facets.valueChanges
      .pipe(
        debounceTime(1000),
        map(v => (v ?? {})),
        distinctUntilChanged((a,b) => facetsEqual(a,b)),
      )
      .subscribe(facetsValue => {
      // facetsValue: Record<facetName, Record<valueName, boolean>>
      const filters: FacetFilter[] = Object.entries(facetsValue ?? {})
        .map(([facetName, selectedValues]) => ({
          facetName,
          values: (selectedValues ?? []).filter(v => typeof v === 'string' && v.length > 0),
        })).filter(f => f.values.length > 0);
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
      tap(_ => console.log(_.facetFilters)),
      distinctUntilChanged(),
      switchMap(body =>
        this.http.post<GetFacetStatsResult>('/api/sighting/list/stats', body, {withCredentials: true})),
      map(facetResults => this.mergeStats(facetResults.stats, this.facetFields().stats)),
      tap(stats => {
        for (const [facetName] of stats.entries()) {
          this.facetSelectionControl(facetName);
        }
        this.sanitizeSelectionsAgainstFacetFields();
      }),
      shareReplay({bufferSize: 1, refCount: true})
    );


    this.destroyRef.onDestroy(() => {
      sub1.unsubscribe();
      sub2.unsubscribe();
      sub3.unsubscribe();
    });
  }

  facetSelectionControl(facetName: string): FormControl<string[]> {
    const facets = this.searchForm.controls.facets;
    const existing = facets.controls[facetName];
    if (existing) {
      return existing;
    }
    const created = new FormControl<string[]>([], {nonNullable: true});
    facets.addControl(facetName, created);
    return created;
  }

  isSelected(facetName: string, valueName: string): boolean {
    const selected = this.searchForm.controls.facets.controls[facetName]?.value ?? [];
    return selected.includes(valueName);
  }

  private ensureFacetControlsFromRecord(record: FacetStatsMap): void {
    for (const facetName of Object.keys(record ?? {})) {
      this.facetSelectionControl(facetName);
    }
  }

  private mergeStats(stats: FacetStatsMap, extraVals: FacetStatsMap): Map<string, ValueCountPair[]> {
    const response = new Map<string, ValueCountPair[]>();

    for (const [facetName, allowedPairs] of Object.entries(extraVals ?? {})) {
      const serverPairs = (stats ?? {})[facetName] ?? [];
      const merged = (allowedPairs ?? []).map(ap => {
        const found = serverPairs.find(sp => sp.value === ap.value);
        return found ?? ({value: ap.value, count: 0} as ValueCountPair);
      });
      response.set(facetName, merged);
    }

    return response;
  }

  clearValues = (facetName: string) => this.searchForm.controls.facets.controls[facetName].reset();
}

function arrayEqual(a: string[], b: string[]): boolean {
  if (a === b) return true;
  if (!a || !b) return false;
  if (a.length !== b.length) return false;
  for (let i = 0; i < a.length; i += 1) {
    if (a[i] !== b[i]) return false;
  }
  return true;
}

function facetsEqual(
  a: Partial<Record<string, string[]>>,
  b: Partial<Record<string, string[]>>
): boolean {
  const aKeys = Object.keys(a);
  const bKeys = Object.keys(b);
  if (aKeys.length !== bKeys.length) return false;

  for (const key of aKeys) {
    if (!(key in b)) return false;
    if (!stringArraySetEqual(a[key] ?? [], b[key] ?? [])) return false;
  }
  return true;
}

function stringArraySetEqual(a: string[], b: string[]): boolean {
  if (a.length !== b.length) return false;
  const as = new Set(a);
  if (as.size !== b.length) return false; // catches duplicates mismatch
  for (const x of b) if (!as.has(x)) return false;
  return true;
}
