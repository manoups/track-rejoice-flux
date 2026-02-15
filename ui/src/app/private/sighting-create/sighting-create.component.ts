import {Component, inject, input, signal} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {View} from '../../common/view';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {MatError, MatFormField, MatLabel} from '@angular/material/form-field';
import {MatCheckbox} from '@angular/material/checkbox';
import {MatInput} from '@angular/material/input';
import {MatOption} from '@angular/material/core';
import {MatSelect} from '@angular/material/select';
import {PrintErrorPipe} from '../../common/print-error-pipe';

@Component({
  selector: 'track-rejoice-sighting-create',
  imports: [
    FormsModule,
    RouterLink,
    MatButton,
    MatCheckbox,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatInput,
    MatOption,
    MatSelect,
    MatError,
    PrintErrorPipe
  ],
  templateUrl: './sighting-create.component.html',
  styleUrl: './sighting-create.component.css',
})
export class SightingCreateComponent extends View {
  router: Router = inject(Router);
  submitting = signal(false);
  hideRequired = input(true);
  floatLabel = input<'auto' | 'always'>('auto');
  form = new FormGroup({
    sightingDetails: new FormGroup({
      lat: new FormControl<number>(null, [Validators.required]),
      lng: new FormControl<number>(null, [Validators.required])
    }),
    subtype: new FormControl('cat', [Validators.required]),
    removeAfterMatching: new FormControl(false),
  })

  create(): void {
    if (this.form.invalid) {
      return;
    }

    this.submitting.set(true);

    this.sendCommand(
      '/api/sighting',
      this.form.value,
      () => this.router.navigate(['/sightings'], {replaceUrl: true}),
      () => this.submitting.set(false),
      {eventOnSuccess: true}
    );
  }
}
