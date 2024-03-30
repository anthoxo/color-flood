import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { DevComponent } from './dev/dev.component';
import { SoloComponent } from './solo/solo.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'dev', component: DevComponent },
  { path: 'solo', component: SoloComponent },
];
