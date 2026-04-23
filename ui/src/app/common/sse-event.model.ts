import {WeightedAssociationState} from '@trackrejoice/typescriptmodels';

export interface SseEvent {
  changeType: 'INITIAL' | 'CREATED' | 'UPDATED' | 'DELETED';
  payload: WeightedAssociationState | { weightedAssociationId: string } | null;
}
