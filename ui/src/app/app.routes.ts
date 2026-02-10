import {Routes} from '@angular/router';
import {ContentsComponent} from './private/contents/contents.component';
import {SightingCreateComponent} from './private/sighting-create/sighting-create.component';
import {ContentCreateComponent} from './private/content-create/content-create.component';
import {SightingDetailsComponent} from './private/sighting-details/sighting-details.component';
import {SightingBoilerplateComponent} from './private/sighting-boilerplate/sighting-boilerplate.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'sightings' },

  { path: 'sightings', component: SightingBoilerplateComponent },
  { path: 'sightings/:id', component: SightingDetailsComponent },
  { path: 'sightings/new', component: SightingCreateComponent },

  { path: 'contents', component: ContentsComponent },
  { path: 'contents/new', component: ContentCreateComponent },

  { path: '**', redirectTo: 'sightings' }
];
