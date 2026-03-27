import {Component, signal} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {ReactiveFormsModule} from '@angular/forms';
import {LoginComponent} from '../login/login.component';
import {SignupComponent} from '../signup/signup.component';

@Component({
  selector: 'track-rejoice-auth',
  imports: [MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule, ReactiveFormsModule, LoginComponent, SignupComponent],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css',
})
export class AuthComponent {

  isLoginMode = signal(true);

  onSwitchMode() {
    this.isLoginMode.set(!this.isLoginMode());
  }

}
