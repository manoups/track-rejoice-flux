import {Component, ElementRef, input, output} from '@angular/core';
import {AlertLevel} from '../alert';

@Component({
  selector: 'track-rejoice-status-alert',
  imports: [],
  templateUrl: './status-alert.component.html',
  styleUrl: './status-alert.component.css',
})
export class StatusAlertComponent {
  type=input<AlertLevel>();
  msShowTime = input<number>();
  close = output();

  constructor(private elementRef: ElementRef) {
  }

  ngOnInit() {
    if (!this.type) {
      throw new Error('Attribute type is required for app-status-alert component');
    }
  }

  ngAfterViewInit(): void {
    const element = $(this.elementRef.nativeElement.querySelector(".alert"));
    element.on('closed.bs.alert', () => {
      this.close.emit();
    });
    if (this.msShowTime()) {
      setTimeout(() => element.alert('close'), this.msShowTime());
    }
  }
}
