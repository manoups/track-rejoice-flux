import { Component } from '@angular/core';
import {AppCommonUtils} from '../app-common-utils';
import {StatusAlertComponent} from './status-alert/status-alert.component';

@Component({
  selector: 'track-rejoice-alerting',
  imports: [
    StatusAlertComponent
  ],
  templateUrl: './alerting.component.html',
  styleUrl: './alerting.component.css',
})
export class AlertingComponent {
  appCommonUtils = AppCommonUtils;
  maxAlertCount = 2;
}
