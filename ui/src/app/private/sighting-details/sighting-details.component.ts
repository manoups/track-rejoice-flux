import {Component, inject, OnInit, signal} from '@angular/core';
import {Sighting} from '@trackrejoice/typescriptmodels';
import {View} from '../../common/view';
import {map, Observable, switchMap} from 'rxjs';
import {AsyncPipe, JsonPipe} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'track-rejoice-sighting-details',
  imports: [
    AsyncPipe,
    JsonPipe
  ],
  templateUrl: './sighting-details.component.html',
  styleUrl: './sighting-details.component.css',
})
export class SightingDetailsComponent extends View implements OnInit {
  data$!: Observable<Sighting>;
  sightingId = signal<string>('');
  private http = inject(HttpClient);

  constructor(private readonly route: ActivatedRoute) {
    super();
  }

  ngOnInit(): void {
    this.data$ = this.route.paramMap
      .pipe(map(params => params.get('id') ?? ''),
        switchMap(id => this.http.get<Sighting>(`/api/sighting/${encodeURIComponent(id)}`, {withCredentials: true})))
      /*.subscribe({
        next: (rows) => {
          const nextRows = rows ?? [];
          this.done.set(true);
          this.loading.set(false);
        },
        error: () => {
          this.loading.set(false);
        }
      });*/
  }
}
