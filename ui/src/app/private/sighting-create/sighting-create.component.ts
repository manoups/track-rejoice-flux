import {Component, inject, signal} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {View} from '../../common/view';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {FloatLabelType, MatError, MatFormField, MatLabel} from '@angular/material/form-field';
import {toSignal} from '@angular/core/rxjs-interop';
import {map} from 'rxjs';
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
  submitting = signal(false);


  readonly hideRequiredControl = new FormControl(false);
  readonly floatLabelControl = new FormControl('auto' as FloatLabelType);
  readonly options = inject(FormBuilder).group({
    hideRequired: this.hideRequiredControl,
    floatLabel: this.floatLabelControl,
  });
  protected readonly hideRequired = toSignal(this.hideRequiredControl.valueChanges);
  protected readonly floatLabel = toSignal(
    this.floatLabelControl.valueChanges.pipe(map(v => v || 'auto')),
    {initialValue: 'auto'},
  );
  form = new FormGroup({
    sightingDetails: new FormGroup({
      lat: new FormControl<number>(null, [Validators.required]),
      lng: new FormControl<number>(null, [Validators.required])
    }),
    subtype: new FormControl('cat', [Validators.required]),
    removeAfterMatching: new FormControl(false),
  })

  constructor(private router: Router) {
    super();
  }

  create(): void {
    if (this.form.invalid) {return;}

    this.submitting.set(true);

    this.sendCommand(
      '/api/sighting',
      this.form.value,
      () => this.router.navigateByUrl('/sightings'),
      () => this.submitting.set(false),
      {eventOnSuccess: true}
    );
  }
}
