import {Routes} from '@angular/router';
import {ContentsComponent} from './private/contents/contents.component';
import {SightingCreateComponent} from './private/sighting-create/sighting-create.component';
import {ContentCreateComponent} from './private/content-create/content-create.component';
import {SightingDetailsComponent} from './private/sighting-details/sighting-details.component';
import {
  resolveFilters,
  SightingBoilerplateComponent
} from './private/sighting-boilerplate/sighting-boilerplate.component';
import {WelcomeComponent} from './welcome/welcome.component';

export const routes: Routes = [
  {path: '', pathMatch: 'full', redirectTo: 'welcome'},
  {path: 'welcome', component: WelcomeComponent},
  {
    path: 'sightings',
    component: SightingBoilerplateComponent,
    data: {statsEndpoint: '/api/sighting/list/stats'},
    resolve: {
      initFilterValues: resolveFilters
    }
  },
  {path: 'sightings/new', component: SightingCreateComponent},
  {
    path: 'sightings/:id',
    component: SightingDetailsComponent,
    data: {title: 'Sighting Matches', statsEndpoint: '/api/sighting/list/stats'},
    title: 'Sighting Matches'
  },

  {path: 'contents', component: ContentsComponent},
  {path: 'contents/new', component: ContentCreateComponent},
  {path: '**', redirectTo: 'sightings'}
];
