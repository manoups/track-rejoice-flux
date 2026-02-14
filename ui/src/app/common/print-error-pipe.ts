import {Pipe, PipeTransform} from '@angular/core';
import {AbstractControl, AbstractControlDirective} from '@angular/forms';

@Pipe({
  name: 'printerror',
})
export class PrintErrorPipe implements PipeTransform {

  transform(control: AbstractControl | AbstractControlDirective, ...args: unknown[]): unknown {
    if (control.errors['required']) {
      return "This field is required";
    } else if (control.errors['unique']) {
      return control.errors['unique'];
    } else if (control.errors['maxlength']) {
      return `Maximum ${control.errors['maxlength'].requiredLength} characters are allowed`;
    } else if (control.errors['minlength']) {
      return `Minimum ${control.errors['minlength'].requiredLength} characters are required`;
    }
    return null;
  }
}
