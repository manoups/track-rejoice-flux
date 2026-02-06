import { Routes } from '@angular/router';
import {SightingsComponent} from './private/sightings/sightings.component';
import {ContentsComponent} from './private/contents/contents.component';
import {SightingCreateComponent} from './private/sighting-create/sighting-create.component';
import {ContentCreateComponent} from './private/content-create/content-create.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'sightings' },

  { path: 'sightings', component: SightingsComponent },
  { path: 'sightings/new', component: SightingCreateComponent },

  { path: 'contents', component: ContentsComponent },
  { path: 'contents/new', component: ContentCreateComponent },

  { path: '**', redirectTo: 'sightings' }
];
