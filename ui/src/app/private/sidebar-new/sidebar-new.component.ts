import {Component, computed, effect, inject, input, output} from '@angular/core';
import {SidebarNewService} from './sidebar-new.service';
import {RouterLink} from '@angular/router';
import {FacetFilter, FacetPaginationRequestBody, ValueCountPair} from '@trackrejoice/typescriptmodels';
import {debounceTime, distinctUntilChanged, map} from 'rxjs';
import {rxResource, toSignal} from '@angular/core/rxjs-interop';
import {FormControl, FormGroup, FormRecord, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {KeyValuePipe, TitleCasePipe} from '@angular/common';
import {MatButton} from '@angular/material/button';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatListOption, MatSelectionList} from '@angular/material/list';

@Component({
  selector: 'track-rejoice-sidebar-new',
  imports: [
    FormsModule,
    KeyValuePipe,
    MatButton,
    MatFormField,
    MatInput,
    MatLabel,
    MatListOption,
    MatSelectionList,
    ReactiveFormsModule,
    RouterLink,
    TitleCasePipe
  ],
  templateUrl: './sidebar-new.component.html',
  styleUrl: './sidebar-new.component.css',
})
export class SidebarNewComponent {
  service = inject(SidebarNewService)
  defaultParams: FacetPaginationRequestBody = {facetFilters: [], filter: "", pagination: {page: 0, pageSize: 10}};
  statsEndpoint = input.required<string>();
  searchForm = new FormGroup({
    search: new FormControl<string>(''),
    facets: new FormRecord<FormControl<string[]>>({}),

  });
  private initialFacetMap: Map<string, ValueCountPair[]>;
  filterChange = output<[string, FacetFilter[]]>();
  term = toSignal(
    this.searchForm.controls.search.valueChanges.pipe(
      map(v => (v ?? '').trim()),
      debounceTime(500),
      distinctUntilChanged()
    ),
    {initialValue: ''}
  )

  facetFilters = toSignal(this.searchForm.controls.facets.valueChanges
    .pipe(
      map(v => (v ?? {})),
      debounceTime(1000),
      distinctUntilChanged((a, b) => facetsEqual(a, b)),
      map(facetsValue => Object.entries(facetsValue ?? {})
        .map(([facetName, selectedValues]) => ({
          facetName,
          values: (selectedValues ?? []).filter(v => typeof v === 'string' && v.length > 0),
        }))
        .filter(f => f.values.length > 0) as FacetFilter[])
    ), {initialValue: []});

  requestParams = computed(() => ({...this.defaultParams, filter: this.term()}))

  valueMapResource = rxResource({
    params: () => this.requestParams(),
    stream: ({params}) => this.service.getStats(params.filter, params.facetFilters, this.statsEndpoint())
  });

  valueMap = computed(() => this.valueMapResource.hasValue() ? this.mergeWithInitialValues(this.valueMapResource.value()) : undefined)

  constructor() {
    effect(() => {
      this.filterChange.emit([this.term(), this.facetFilters()]);
      this.syncFacetControls(this.valueMap());
    });
  }

  private mergeWithInitialValues(stats: Record<string, ValueCountPair[]>): Map<string, ValueCountPair[]> {
    if (this.initialFacetMap === undefined) {
      const filterMap = new Map<string, ValueCountPair[]>();
      for (const [facetName, allowedPairs] of Object.entries(stats ?? {})) {
        filterMap.set(facetName, allowedPairs);
      }
      this.initialFacetMap = filterMap;
      return filterMap;
    }
    const merged = new Map<string, ValueCountPair[]>();

    for (const [facetName, initialPairs] of this.initialFacetMap) {
      const fetchedPairs = stats?.[facetName] ?? [];

      merged.set(
        facetName,
        (initialPairs ?? []).map(initialPair => {
          const found = fetchedPairs.find(
            fetched => fetched.value.toLowerCase() === initialPair.value.toLowerCase()
          );

          return found ?? ({value: initialPair.value, count: 0} as ValueCountPair);
        })
      );
    }

    return merged;
  }

  private syncFacetControls(facetMap: Map<string, ValueCountPair[]> | undefined): void {
    if (facetMap === undefined) return;
    const facets = this.searchForm.controls.facets;

    for (const facetName of Object.keys(facets.controls)) {
      if (!facetMap.has(facetName)) {
        facets.removeControl(facetName, {emitEvent: false});
      }
    }

    for (const [facetName, values] of facetMap) {
      const allowedValues = values.map(v => v.value);
      const existing = facets.controls[facetName];

      if (!existing) {
        facets.addControl(
          facetName,
          new FormControl<string[]>(allowedValues, {nonNullable: true}),
          {emitEvent: false}
        );
        continue;
      }

      const currentValue = existing.getRawValue() ?? [];
      const nextValue = currentValue.filter(value => allowedValues.includes(value));

      if (
        currentValue.length !== nextValue.length ||
        currentValue.some((value, index) => value !== nextValue[index])
      ) {
        existing.setValue(nextValue, {emitEvent: false});
      }
    }
  }

  resetValues = (facetName: string) => {
    const control = this.searchForm.controls.facets.controls[facetName];
    const initialValues = this.valueMap().get(facetName)?.map(v => v.value) ?? [];
    control?.setValue(initialValues);
  };
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
