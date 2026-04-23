import * as fc from 'fast-check';
import {ComponentFixture, fakeAsync, flushMicrotasks, TestBed} from '@angular/core/testing';
import {WeightedAssociationState} from '@trackrejoice/typescriptmodels';
import {ProposalsTableComponent} from './proposals-table.component';
import {SseService} from '../../../common/sse.service';
import {SseEvent} from '../../../common/sse-event.model';
import {TrackRejoiceDataSource} from '../../../common/datasource';
import {Subject} from 'rxjs';

// Arbitrary for generating random WeightedAssociationState records
const weightedAssociationStateArbitrary: fc.Arbitrary<WeightedAssociationState> = fc.record({
  weightedAssociationId: fc.uuid(),
  contentId: fc.uuid(),
  sightingId: fc.uuid(),
  status: fc.constantFrom('CREATED', 'LINKED', 'ACCEPTED', 'REJECTED'),
  distance: fc.double({min: 0, max: 1000, noNaN: true, noDefaultInfinity: true}),
  score: fc.double({min: 0, max: 1000, noNaN: true, noDefaultInfinity: true}),
});

describe('ProposalsTableComponent', () => {

  // Feature: pet-content-sse-dashboard, Property 7: Proposals table snapshot populates all rows with correct columns
  // Validates: Requirements 6.1, 6.2
  describe('Property 7: Proposals table snapshot populates all rows with correct columns', () => {
    let sseSubject: Subject<SseEvent>;
    let sseSpy: jasmine.SpyObj<SseService>;

    beforeEach(() => {
      sseSubject = new Subject<SseEvent>();
      sseSpy = jasmine.createSpyObj('SseService', ['connect']);
      sseSpy.connect.and.returnValue(sseSubject.asObservable());

      TestBed.configureTestingModule({
        imports: [ProposalsTableComponent],
        providers: [
          {provide: SseService, useValue: sseSpy},
        ],
      });
    });

    it('should display exactly N rows with correct column values for any snapshot of N records', fakeAsync(() => {
      fc.assert(
        fc.property(
          fc.array(weightedAssociationStateArbitrary, {minLength: 0, maxLength: 15}),
          (records: WeightedAssociationState[]) => {
            const fixture: ComponentFixture<ProposalsTableComponent> = TestBed.createComponent(ProposalsTableComponent);
            fixture.componentRef.setInput('contentId', 'test-content-id');
            fixture.detectChanges();

            // Emit all records as INITIAL events (snapshot)
            for (const record of records) {
              sseSubject.next({changeType: 'INITIAL', payload: record});
            }

            // Flush the queueMicrotask used by the component's snapshot batching
            flushMicrotasks();
            fixture.detectChanges();

            // Verify row count
            const rows = fixture.nativeElement.querySelectorAll('tr.mat-mdc-row');
            expect(rows.length).toBe(records.length);

            // Verify each row's column values
            for (let i = 0; i < records.length; i++) {
              const cells = rows[i].querySelectorAll('td.mat-mdc-cell');
              expect(cells.length).toBe(5);
              expect(cells[0].textContent.trim()).toBe(String(records[i].weightedAssociationId ?? ''));
              expect(cells[1].textContent.trim()).toBe(String(records[i].sightingId ?? ''));
              expect(cells[2].textContent.trim()).toBe(String(records[i].status ?? ''));
              expect(cells[3].textContent.trim()).toBe(String(records[i].distance ?? ''));
              expect(cells[4].textContent.trim()).toBe(String(records[i].score ?? ''));
            }

            fixture.destroy();

            // Reset the subject for the next iteration
            sseSubject = new Subject<SseEvent>();
            sseSpy.connect.and.returnValue(sseSubject.asObservable());
          }
        ),
        {numRuns: 20}
      );
    }));
  });

  // Feature: pet-content-sse-dashboard, Property 8: Proposals table mutations produce correct final state
  // Validates: Requirements 6.3, 6.4, 6.5
  describe('Property 8: Proposals table mutations produce correct final state', () => {

    type MutationEvent =
      | { type: 'CREATED'; record: WeightedAssociationState }
      | { type: 'UPDATED'; record: WeightedAssociationState }
      | { type: 'DELETED'; weightedAssociationId: string };

    /**
     * Generates a sequence of mutation events that are valid relative to the initial state:
     * - CREATED adds a new record with a fresh ID
     * - UPDATED picks an existing ID and replaces the record
     * - DELETED picks an existing ID and removes it
     */
    function mutationEventsArbitrary(initial: WeightedAssociationState[]): fc.Arbitrary<MutationEvent[]> {
      return fc.array(
        fc.oneof(
          // CREATED: always valid — generates a brand new record
          weightedAssociationStateArbitrary.map(record => ({type: 'CREATED' as const, record})),
          // UPDATED: pick index into current state (resolved at apply-time via chain)
          fc.record({
            targetIndex: fc.nat(),
            replacement: weightedAssociationStateArbitrary,
          }).map(({targetIndex, replacement}) => ({type: 'UPDATED' as const, targetIndex, replacement})),
          // DELETED: pick index into current state (resolved at apply-time)
          fc.nat().map(targetIndex => ({type: 'DELETED' as const, targetIndex}))
        ),
        {minLength: 0, maxLength: 20}
      ) as fc.Arbitrary<any[]>;
    }

    /**
     * Applies mutation events to a plain array (the "oracle") and to the data source,
     * resolving index-based references against the current oracle state.
     */
    function applyMutations(
      initial: WeightedAssociationState[],
      rawEvents: any[],
      dataSource: TrackRejoiceDataSource<WeightedAssociationState>
    ): WeightedAssociationState[] {
      const expected = [...initial];

      for (const event of rawEvents) {
        if (event.type === 'CREATED') {
          const record = event.record as WeightedAssociationState;
          expected.push(record);
          dataSource.append([record]);
        } else if (event.type === 'UPDATED') {
          if (expected.length === 0) continue; // skip if nothing to update
          const idx = event.targetIndex % expected.length;
          const targetId = (expected[idx] as any).weightedAssociationId;
          // Build replacement with the target's ID so it matches
          const replacement: WeightedAssociationState = {
            ...event.replacement,
            weightedAssociationId: targetId,
          };
          expected[idx] = replacement;
          dataSource.update(
            (row) => (row as any).weightedAssociationId === targetId,
            replacement
          );
        } else if (event.type === 'DELETED') {
          if (expected.length === 0) continue; // skip if nothing to delete
          const idx = event.targetIndex % expected.length;
          const targetId = (expected[idx] as any).weightedAssociationId;
          expected.splice(idx, 1);
          dataSource.remove(
            (row) => (row as any).weightedAssociationId === targetId
          );
        }
      }

      return expected;
    }

    it('should produce correct final state after applying random CREATED/UPDATED/DELETED mutations', () => {
      fc.assert(
        fc.property(
          fc.array(weightedAssociationStateArbitrary, {minLength: 0, maxLength: 10}),
          fc.array(
            fc.oneof(
              weightedAssociationStateArbitrary.map(record => ({type: 'CREATED' as const, record})),
              fc.record({
                targetIndex: fc.nat(),
                replacement: weightedAssociationStateArbitrary,
              }).map(({targetIndex, replacement}) => ({type: 'UPDATED' as const, targetIndex, replacement})),
              fc.nat().map(targetIndex => ({type: 'DELETED' as const, targetIndex}))
            ),
            {minLength: 1, maxLength: 20}
          ),
          (initialRecords, rawEvents) => {
            const dataSource = new TrackRejoiceDataSource<WeightedAssociationState>();

            // Set initial state
            dataSource.set([...initialRecords]);

            // Apply mutations to both oracle and data source
            const expectedState = applyMutations(initialRecords, rawEvents, dataSource);

            // Verify length matches
            expect(dataSource.length).toBe(expectedState.length);

            // Verify each record matches by position
            const actualData = dataSource.data;
            for (let i = 0; i < expectedState.length; i++) {
              expect((actualData[i] as any).weightedAssociationId)
                .toBe((expectedState[i] as any).weightedAssociationId);
              expect((actualData[i] as any).sightingId)
                .toBe((expectedState[i] as any).sightingId);
              expect((actualData[i] as any).status)
                .toBe((expectedState[i] as any).status);
              expect((actualData[i] as any).distance)
                .toBe((expectedState[i] as any).distance);
              expect((actualData[i] as any).score)
                .toBe((expectedState[i] as any).score);
            }

            dataSource.disconnect();
          }
        ),
        {numRuns: 20}
      );
    });
  });
});
