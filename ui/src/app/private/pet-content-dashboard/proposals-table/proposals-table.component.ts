import {Component, DestroyRef, inject, input, OnInit, signal} from '@angular/core';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable
} from '@angular/material/table';
import {TrackRejoiceDataSource} from '../../../common/datasource';
import {WeightedAssociationState} from '@trackrejoice/typescriptmodels';
import {SseService} from '../../../common/sse.service';
import {SseEvent} from '../../../common/sse-event.model';

@Component({
  selector: 'track-rejoice-proposals-table',
  imports: [
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderCellDef,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
    MatTable,
  ],
  templateUrl: './proposals-table.component.html',
  styleUrl: './proposals-table.component.css',
})
export class ProposalsTableComponent implements OnInit {
  private sseService = inject(SseService);
  private destroyRef = inject(DestroyRef);

  contentId = input.required<string>();
  dataSource = new TrackRejoiceDataSource<WeightedAssociationState>();
  displayedColumns = ['weightedAssociationId', 'sightingId', 'status', 'distance', 'score'];
  trackByWeightedAssociationId = (_index: number, row: WeightedAssociationState) => (row as any).weightedAssociationId;
  disconnected = signal(false);
  highlightedIds = signal<Set<string>>(new Set());

  private snapshotBuffer: WeightedAssociationState[] = [];
  private receivingSnapshot = false;

  ngOnInit(): void {
    const subscription = this.sseService
      .connect(`/api/content/${encodeURIComponent(this.contentId())}/proposals/stream`)
      .subscribe((event: SseEvent) => {
        const changeType = event.changeType as string;

        if (changeType === 'DISCONNECTED') {
          this.disconnected.set(true);
          return;
        }

        this.disconnected.set(false);

        if (event.changeType === 'INITIAL') {
          if (!this.receivingSnapshot) {
            this.receivingSnapshot = true;
            this.snapshotBuffer = [];
            // Flush after all synchronous INITIAL events have been emitted
            queueMicrotask(() => {
              this.dataSource.set(this.snapshotBuffer);
              this.snapshotBuffer = [];
              this.receivingSnapshot = false;
            });
          }
          this.snapshotBuffer.push(event.payload as WeightedAssociationState);
          return;
        }

        switch (event.changeType) {
          case 'CREATED':
            this.handleCreated(event);
            break;
          case 'UPDATED':
            this.handleUpdated(event);
            break;
          case 'DELETED':
            this.handleDeleted(event);
            break;
        }
      });

    this.destroyRef.onDestroy(() => subscription.unsubscribe());
  }

  private handleCreated(event: SseEvent): void {
    const record = event.payload as WeightedAssociationState;
    this.dataSource.append([record]);
    this.highlightRow((record as any).weightedAssociationId);
  }

  private handleUpdated(event: SseEvent): void {
    const record = event.payload as WeightedAssociationState;
    const id = (record as any).weightedAssociationId;
    this.dataSource.update(
      (row) => (row as any).weightedAssociationId === id,
      record
    );
    this.highlightRow(id);
  }

  private handleDeleted(event: SseEvent): void {
    const payload = event.payload as { weightedAssociationId: string };
    this.dataSource.remove(
      (row) => (row as any).weightedAssociationId === payload.weightedAssociationId
    );
  }

  private highlightRow(id: string): void {
    this.highlightedIds.update(ids => {
      const next = new Set(ids);
      next.add(id);
      return next;
    });
    setTimeout(() => {
      this.highlightedIds.update(ids => {
        const next = new Set(ids);
        next.delete(id);
        return next;
      });
    }, 2000);
  }

  isHighlighted(row: WeightedAssociationState): boolean {
    return this.highlightedIds().has((row as any).weightedAssociationId);
  }
}
