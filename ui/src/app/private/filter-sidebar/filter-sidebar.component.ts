import {Component, Input} from '@angular/core';
import {Observable} from 'rxjs';
import {GetFacetStatsResult} from '@trackrejoice/typescriptmodels';
import {AsyncPipe} from '@angular/common';

@Component({
  selector: 'track-rejoice-filter-sidebar',
  imports: [
    AsyncPipe
  ],
  templateUrl: './filter-sidebar.component.html',
  styleUrl: './filter-sidebar.component.css',
})
export class FilterSidebarComponent {
  @Input({ required: true }) input!: Observable<GetFacetStatsResult>;
}
