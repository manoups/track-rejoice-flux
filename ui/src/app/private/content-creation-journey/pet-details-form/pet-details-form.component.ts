import {Component, inject, signal} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {View} from '../../../common/view';
import {MatButton} from '@angular/material/button';
import {MatError, MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatOption} from '@angular/material/core';
import {MatSelect} from '@angular/material/select';
import {PrintErrorPipe} from '../../../common/print-error-pipe';
import {MapboxLocationPickerComponent} from '../mapbox-location-picker/mapbox-location-picker.component';

@Component({
  selector: 'track-rejoice-pet-details-form',
  imports: [
    ReactiveFormsModule,
    MatButton,
    MatFormField,
    MatLabel,
    MatInput,
    MatOption,
    MatSelect,
    MatError,
    PrintErrorPipe,
    MapboxLocationPickerComponent,
  ],
  templateUrl: './pet-details-form.component.html',
  styleUrl: './pet-details-form.component.css',
})
export class PetDetailsFormComponent extends View {
  private router = inject(Router);
  submitting = signal(false);
  errorMessage = signal<string | null>(null);

  form = new FormGroup({
    sightingDetails: new FormGroup({
      lng: new FormControl<number>(null, [Validators.required]),
      lat: new FormControl<number>(null, [Validators.required]),
    }),
    details: new FormGroup({
      '@class': new FormControl('Pet'),
      subtype: new FormControl<string>(null, [Validators.required]),
      name: new FormControl<string>(null, [Validators.required]),
      breed: new FormControl<string>(null, [Validators.required]),
      gender: new FormControl<string>(null, [Validators.required]),
      age: new FormControl<string>(null),
      size: new FormControl<string>(null),
      color: new FormControl<string>(null),
      condition: new FormControl<string>(null),
      description: new FormControl<string>(null),
      location: new FormControl<string>(null),
      image: new FormControl<string>(null),
    }),
  });

  onLocationSelected(coords: { lng: number; lat: number }): void {
    this.form.controls.sightingDetails.patchValue({
      lng: coords.lng,
      lat: coords.lat,
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting.set(true);
    this.errorMessage.set(null);

    this.sendCommand(
      '/api/content',
      this.form.value,
      (contentId) => {
        const id = typeof contentId === 'string' ? contentId : (contentId?.id ?? contentId?.value ?? contentId);
        this.router.navigate(['/content/new/payment', id], {replaceUrl: true});
      },
      () => {
        this.submitting.set(false);
        this.errorMessage.set('Failed to create content. Please try again.');
      },
      {eventOnSuccess: true}
    );
  }
}
