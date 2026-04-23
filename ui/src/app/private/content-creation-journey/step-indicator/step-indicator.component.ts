import {Component, Input} from '@angular/core';

@Component({
  selector: 'track-rejoice-step-indicator',
  templateUrl: './step-indicator.component.html',
  styleUrl: './step-indicator.component.css',
})
export class StepIndicatorComponent {
  @Input() currentStep: number = 1;

  steps = [
    {number: 1, label: 'Pet Details'},
    {number: 2, label: 'Payment'},
  ];
}
