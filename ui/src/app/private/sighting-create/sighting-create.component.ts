import {Component, signal} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {View} from '../../common/view';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'track-rejoice-sighting-create',
  imports: [
    FormsModule,
    RouterLink
  ],
  templateUrl: './sighting-create.component.html',
  styleUrl: './sighting-create.component.css',
})
export class SightingCreateComponent extends View {
  removeAfterMatching = signal(false);
  lat = signal<number | null>(null);
  lng = signal<number | null>(null);
  submitting = signal(false);

  constructor(private router: Router) {
    super();
  }

  create(): void {
    const lat = this.lat();
    const lng = this.lng();

    if (lat == null || Number.isNaN(lat) || lng == null || Number.isNaN(lng)) {
      alert('Please enter valid numeric lat and lng values');
      return;
    }

    this.submitting.set(true);

    const payload = {
        sightingDetails: {
          lng,
          lat
        },
      removeAfterMatching: this.removeAfterMatching()
    };

    this.sendCommand(
      '/api/sighting',
      payload,
      () => this.router.navigateByUrl('/sightings'),
      () => this.submitting.set(false),
      {eventOnSuccess: true}
    );
  }
}
