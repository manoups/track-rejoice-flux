import {Component, effect, inject, Renderer2, signal} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {LoadingIndicatorComponent} from './common/loading-indicator/loading-indicator.component';
import {MatSlideToggle} from '@angular/material/slide-toggle';
import {MatIcon} from '@angular/material/icon';
import {MatIconButton} from '@angular/material/button';
import {MatToolbar} from '@angular/material/toolbar';

type ThemeMode = 'system' | 'light' | 'dark';

@Component({
  selector: 'track-rejoice-root',
  imports: [
    RouterOutlet,
    LoadingIndicatorComponent,
    MatSlideToggle,
    MatIcon,
    MatIconButton,
    MatToolbar
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'track-rejoice-ui';
  public readonly theme = signal<ThemeMode>('system');
  private isDarkTheme = false;

  private readonly _renderer = inject(Renderer2);

  constructor() {
    this.isDarkTheme = window.matchMedia('(prefers-color-scheme: dark)').matches;
    // this.applyTheme();
    this.theme.set(this.isDarkTheme ? 'dark' : 'light');
    effect(() => this._setTheme(this.theme()));
  }

  private _setTheme(theme: ThemeMode) {
    const colorScheme = theme === 'system' ? 'light dark' : theme;
    this._renderer.setProperty(
      document.body.style,
      'color-scheme',
      colorScheme
    );
  }
}
