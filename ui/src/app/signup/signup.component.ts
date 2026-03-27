import {Component, signal} from '@angular/core';
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatError, MatFormField, MatInput, MatLabel, MatSuffix} from "@angular/material/input";
import {MatIcon} from "@angular/material/icon";
import {AbstractControl, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {merge} from 'rxjs';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {MatCheckbox} from '@angular/material/checkbox';

function equalValues(controlName1: string, controlName2: string) {
  return (control: AbstractControl) => {
    const val1 = control.get(controlName1)?.value;
    const val2 = control.get(controlName2)?.value;
    if (val1 === val2) {
      return null;
    }
    return {valuesNotEqual: true};
  }
}

@Component({
  selector: 'track-rejoice-signup',
  imports: [
    ReactiveFormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    MatError,
    MatIcon,
    MatIconButton,
    MatSuffix,
    MatCheckbox,
    MatButton
  ],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css',
})
export class SignupComponent {
  form = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      passwords: new FormGroup({
          password: new FormControl('', [Validators.required, Validators.minLength(6)]),
          confirmPassword: new FormControl('', [Validators.required, Validators.minLength(6)])
        }, [equalValues('password', 'confirmPassword')]
      ),
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),
    }
  );

  emailErrorMessage = signal('');
  passwordErrorMessage = signal('');
  confirmPasswordErrorMessage = signal('');
  constructor() {
    merge(this.form.controls.email.statusChanges, this.form.controls.email.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updateEmailErrorMessage());
    merge(this.form.controls.passwords.controls.password.statusChanges, this.form.controls.passwords.controls.password.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updatePasswordErrorMessage());
    merge(this.form.controls.passwords.controls.confirmPassword.statusChanges, this.form.controls.passwords.controls.confirmPassword.valueChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.updateConfirmPasswordErrorMessage());
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
    if (this.form.controls.passwords.controls.password.hasError('required')) {
      this.passwordErrorMessage.set('You must enter a value');
    } else if (this.form.controls.passwords.controls.password.hasError('minlength')) {
      this.passwordErrorMessage.set('Minimum 6 characters required');
    } else {
      this.passwordErrorMessage.set('');
    }
  }

  updateConfirmPasswordErrorMessage() {
    if (this.form.controls.passwords.controls.confirmPassword.hasError('required')) {
      this.confirmPasswordErrorMessage.set('You must enter a value');
    } else if (this.form.controls.passwords.controls.confirmPassword.hasError('minlength')) {
      this.confirmPasswordErrorMessage.set('Minimum 6 characters required');
    } else {
      this.confirmPasswordErrorMessage.set('');
    }
  }

  hide = signal(true);
  hideConfirm = signal(true);
  clickEvent(event: MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
  }
  clickEventConfirm(event: MouseEvent) {
    this.hideConfirm.set(!this.hideConfirm());
    event.stopPropagation();
  }
}
