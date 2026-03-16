import { Component } from '@angular/core';
import {
  MatCard, MatCardActions,
  MatCardAvatar,
  MatCardContent,
  MatCardHeader, MatCardImage,
  MatCardSubtitle,
  MatCardTitle
} from '@angular/material/card';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'track-rejoice-welcome',
  imports: [
    MatCardContent,
    MatCardSubtitle,
    MatCardTitle,
    MatCardAvatar,
    MatCardHeader,
    MatCard,
    MatCardImage,
    MatCardActions,
    MatButton
  ],
  templateUrl: './welcome.component.html',
  styleUrl: './welcome.component.css',
})
export class WelcomeComponent {

}
