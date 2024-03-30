import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { SoloComponent } from './solo/solo.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'solo', component: SoloComponent },
];
