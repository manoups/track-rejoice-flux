import {Component, inject, OnInit, signal} from '@angular/core';
import {View} from '../../common/view';
import {HttpClient} from '@angular/common/http';
import {combineLatest, debounceTime, distinctUntilChanged, map, Observable, startWith, switchMap} from 'rxjs';
import {Content} from '@trackrejoice/typescriptmodels';
import {toObservable} from '@angular/core/rxjs-interop';
import {AsyncPipe, JsonPipe} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'track-rejoice-contents',
  imports: [
    AsyncPipe,
    JsonPipe,
    FormsModule,
    RouterLink
  ],
  templateUrl: './contents.component.html',
  styleUrl: './contents.component.css',
})
export class ContentsComponent extends View implements OnInit {
  private http = inject(HttpClient);

  term = signal('');
  private refreshTick = signal(0);

  contents$: Observable<Content[]>;

  ngOnInit(): void {
    const term$ = toObservable(this.term).pipe(
      map(term => term.trim()),
      debounceTime(300),
      distinctUntilChanged()
    );
    const refresh$ = toObservable(this.refreshTick).pipe(startWith(0));

    this.contents$ = combineLatest([term$, refresh$]).pipe(
      switchMap(([term]) => this.sendQuery('/api/content' + (term ? '?term=' + term : '')))
    );
  }

  deleteContent(content: Content): void {
    const id = this.idToString((content as any).contentId);
    if (!id) {
      alert('Cannot delete: missing contentId');
      return;
    }
    if (!confirm(`Delete content ${id}?`)) {
      return;
    }

    this.http.delete(`/api/content/${encodeURIComponent(id)}`, {withCredentials: true})
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
