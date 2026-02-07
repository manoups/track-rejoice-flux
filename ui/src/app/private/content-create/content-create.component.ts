import {Component, signal} from '@angular/core';
import {View} from '../../common/view';
import {Router, RouterLink} from '@angular/router';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'track-rejoice-content-create',
  imports: [
    FormsModule,
    RouterLink
  ],
  templateUrl: './content-create.component.html',
  styleUrl: './content-create.component.css',
})
export class ContentCreateComponent extends View {
  sightingDetailsJson = signal<string>('{}');
  detailsJson = signal<string>('{}');
  submitting = signal(false);

  constructor(private router: Router) {
    super();
  }

  create(): void {
    let sightingDetails: any;
    let details: any;

    try {
      sightingDetails = JSON.parse(this.sightingDetailsJson());
      details = JSON.parse(this.detailsJson());
    } catch {
      alert('sightingDetailsJson and detailsJson must be valid JSON');
      return;
    }

    this.submitting.set(true);

    const payload = {
      sightingDetails,
      details
    };

    this.sendCommand(
      '/api/content',
      payload,
      () => this.router.navigateByUrl('/contents'),
      () => this.submitting.set(false),
      {eventOnSuccess: true}
    );
  }
}
