import {Component, signal} from '@angular/core';
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatError, MatFormField, MatInput, MatLabel, MatSuffix} from "@angular/material/input";
import {MatIcon} from "@angular/material/icon";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {merge} from 'rxjs';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';

@Component({
  selector: 'track-rejoice-login',
    imports: [
        MatButton,
        MatError,
        MatFormField,
        MatIcon,
        MatIconButton,
        MatInput,
        MatLabel,
        MatSuffix,
        ReactiveFormsModule
    ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  form = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(6)])
  });

  emailErrorMessage = signal('');
  passwordErrorMessage = signal('');
  constructor() {
    merge(this.form.controls.email.statusChanges, this.form.controls.email.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updateEmailErrorMessage());
    merge(this.form.controls.password.statusChanges, this.form.controls.password.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updatePasswordErrorMessage());
  }

  updateEmailErrorMessage() {
    if (this.form.controls.email.hasError('required')) {
      this.emailErrorMessage.set('You must enter a value');
    } else if (this.form.controls.email.hasError('email')) {
      this.emailErrorMessage.set('Not a valid email');
    } else {
      this.emailErrorMessage.set('');
    }
  }

  updatePasswordErrorMessage() {
    if (this.form.controls.password.hasError('required')) {
      this.passwordErrorMessage.set('You must enter a value');
    } else if (this.form.controls.password.hasError('minlength')) {
      this.passwordErrorMessage.set('Minimum 6 characters required');
    } else {
      this.passwordErrorMessage.set('');
    }
  }

  hide = signal(true);
  clickEvent(event: MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
  }

  onSubmit() {
    console.log(this.form.value);
    this.form.reset()
  }
}
