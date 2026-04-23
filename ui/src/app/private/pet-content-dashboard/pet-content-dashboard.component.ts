import {Component, inject, input, OnInit, signal} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Content} from '@trackrejoice/typescriptmodels';
import {ContentDetailsPanelComponent} from './content-details-panel/content-details-panel.component';
import {SightingHistoryMapComponent} from './sighting-history-map/sighting-history-map.component';
import {ProposalsTableComponent} from './proposals-table/proposals-table.component';

@Component({
  selector: 'track-rejoice-pet-content-dashboard',
  imports: [ContentDetailsPanelComponent, SightingHistoryMapComponent, ProposalsTableComponent],
  templateUrl: './pet-content-dashboard.component.html',
  styleUrl: './pet-content-dashboard.component.css',
})
export class PetContentDashboardComponent implements OnInit {
  private http = inject(HttpClient);

  id = input.required<string>();
  content = signal<Content | null>(null);
  notFound = signal(false);
  loading = signal(true);

  ngOnInit(): void {
    this.http.get<Content>(`/api/content/${encodeURIComponent(this.id())}`, {withCredentials: true})
      .subscribe({
        next: (content) => {
          this.content.set(content);
          this.loading.set(false);
        },
        error: (err: HttpErrorResponse) => {
          if (err.status === 404) {
            this.notFound.set(true);
          }
          this.loading.set(false);
        }
      });
  }
}
