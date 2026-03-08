import {Component, computed, HostListener, Input, signal} from '@angular/core';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'button[track-rejoice-chevron-button]',
  imports: [
    MatIcon
  ],
  templateUrl: './chevron-button.component.html',
  styleUrl: './chevron-button.component.css',
})
export class ChevronButtonComponent {
  @Input() initialState: "open" | "close" = "close";
  state = signal<"open" | "close">(this.initialState);
  readonly isOpen = computed(() => this.state() === "open");
  @HostListener("click")
  protected toggleState() {
    this.state() === "open" ? this.close() : this.open();
  }

  open() {
    this.state.set("open");
  }

  close() {
    this.state.set("close");
  }
}
