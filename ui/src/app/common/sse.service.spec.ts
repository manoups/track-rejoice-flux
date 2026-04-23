import * as fc from 'fast-check';
import {calculateBackoffDelay} from './sse.service';

describe('SseService', () => {

  // Feature: pet-content-sse-dashboard, Property 9: Exponential backoff delay sequence
  // Validates: Requirements 7.2
  describe('Property 9: Exponential backoff delay sequence', () => {

    it('should compute delay as min(2^(n-1), 30) for any failure count', () => {
      fc.assert(
        fc.property(
          fc.integer({min: 1, max: 50}),
          (failureCount: number) => {
            const expected = Math.min(Math.pow(2, failureCount - 1), 30);
            const actual = calculateBackoffDelay(failureCount);
            expect(actual).toBe(expected);
          }
        ),
        {numRuns: 20}
      );
    });

    it('should start at 1 second for the first failure', () => {
      expect(calculateBackoffDelay(1)).toBe(1);
    });

    it('should cap at 30 seconds', () => {
      fc.assert(
        fc.property(
          fc.integer({min: 6, max: 50}),
          (failureCount: number) => {
            expect(calculateBackoffDelay(failureCount)).toBe(30);
          }
        ),
        {numRuns: 20}
      );
    });
  });
});
