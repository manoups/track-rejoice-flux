import {Component, inject, input, OnInit} from '@angular/core';
import {Sighting} from '@trackrejoice/typescriptmodels';
import {View} from '../../common/view';
import {Observable, switchMap} from 'rxjs';
import {AsyncPipe} from '@angular/common';
import {HttpClient} from '@angular/common/http';
import {toObservable} from '@angular/core/rxjs-interop';
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from '@angular/material/sidenav';
import {MatToolbar} from '@angular/material/toolbar';
import {SightingBasicDetailsComponent} from '../sighting-basic-details/sighting-basic-details.component';
import {MatButton} from '@angular/material/button';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'track-rejoice-sighting-details',
  imports: [
    AsyncPipe,
    MatSidenav,
    MatSidenavContainer,
    MatSidenavContent,
    MatToolbar,
    SightingBasicDetailsComponent,
    MatButton,
    RouterLink
  ],
  templateUrl: './sighting-details.component.html',
  styleUrl: './sighting-details.component.css',
})
export class SightingDetailsComponent extends View implements OnInit {
  data$!: Observable<Sighting>;
  private http = inject(HttpClient);
  id = input.required<string>();
  private id$: Observable<string> = toObservable(this.id);

  ngOnInit(): void {
    this.data$ = this.id$
      .pipe(switchMap(id => this.http.get<Sighting>(`/api/sighting/${encodeURIComponent(id)}`, {withCredentials: true})))
  }
}
