import {Component, OnInit, signal} from '@angular/core';
import {debounceTime, distinctUntilChanged, map, Observable, switchMap} from 'rxjs';
import {View} from '../../common/view';
import {AsyncPipe} from '@angular/common';
import {Sighting} from '@trackrejoice/typescriptmodels';
import {toObservable} from '@angular/core/rxjs-interop';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'track-rejoice-sightings',
  imports: [
    AsyncPipe,
    FormsModule
  ],
  templateUrl: './sightings.component.html',
  styleUrl: './sightings.component.css',
})
export class SightingsComponent extends View implements OnInit {
  term=signal('');
  sightings$: Observable<Sighting[]>;

  ngOnInit(): void {
      this.sightings$ = toObservable(this.term).pipe(
        map(term => term.trim()),
        debounceTime(300),
        distinctUntilChanged(),
        switchMap(term => this.sendQuery('/sighting' + (term ? '?term=' + term : '')))
      );
  }

}
