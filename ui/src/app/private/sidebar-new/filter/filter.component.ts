import {Component, DestroyRef, inject, OnInit, output} from '@angular/core';
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {debounceTime, distinctUntilChanged, map} from 'rxjs';

@Component({
  selector: 'track-rejoice-filter',
  imports: [
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule
  ],
  templateUrl: './filter.component.html',
  styleUrl: './filter.component.css',
})
export class FilterComponent implements OnInit {
  private destroyRef = inject(DestroyRef);
  searchTerm = output<string>();
  form = new FormGroup({
    search: new FormControl<string>(''),
  });

  ngOnInit(): void {
    this.form.controls.search.valueChanges.pipe(
      map(v => (v ?? '').trim()),
      debounceTime(500),
      distinctUntilChanged(),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(value => this.searchTerm.emit(value));
  }
}
