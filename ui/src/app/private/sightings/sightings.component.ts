import {Component, inject, OnInit, signal} from '@angular/core';
import {combineLatest, debounceTime, distinctUntilChanged, map, Observable, startWith, switchMap} from 'rxjs';
import {View} from '../../common/view';
import {AsyncPipe, JsonPipe} from '@angular/common';
import {Sighting} from '@trackrejoice/typescriptmodels';
import {toObservable} from '@angular/core/rxjs-interop';
import {FormsModule} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'track-rejoice-sightings',
  imports: [
    AsyncPipe,
    FormsModule,
    JsonPipe,
    RouterLink
  ],
  templateUrl: './sightings.component.html',
  styleUrl: './sightings.component.css',
})
export class SightingsComponent extends View implements OnInit {
  private http = inject(HttpClient);

  term = signal('');
  private refreshTick = signal(0);
  private term$ = toObservable(this.term).pipe(
    map(term => term.trim()),
    debounceTime(300),
    distinctUntilChanged()
  );
  private refresh$ = toObservable(this.refreshTick).pipe(startWith(0));

  sightings$: Observable<Sighting[]> = combineLatest([this.term$, this.refresh$]).pipe(
    switchMap(([term]) => this.sendQuery('/api/sighting' + (term ? '?term=' + term : '')))
  );

  ngOnInit(): void {

  }

  deleteSighting(sighting: Sighting): void {
    const id = this.idToString((sighting as any).sightingId);
    if (!id) {
      alert('Cannot delete: missing sightingId');
      return;
    }
    if (!confirm(`Delete sighting ${id}?`)) {
      return;
    }

    this.http.delete(`/api/sighting/${encodeURIComponent(id)}`, {withCredentials: true})
      .subscribe({
        next: () => this.refreshTick.update(v => v + 1)
      });
  }

  private idToString(id: any): string {
    if (id == null) return '';
    if (typeof id === 'string' || typeof id === 'number') return String(id);
    if (typeof id === 'object') {
      return String(id.value ?? id.id ?? id.identifier ?? '');
    }
    return String(id);
  }

}
