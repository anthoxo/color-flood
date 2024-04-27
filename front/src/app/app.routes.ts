import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { SoloComponent } from './solo/solo.component';
import { VersusComponent } from './versus/versus.component';
import { BattleRoyaleComponent } from './battle-royale/battle-royale.component';
import { UploadComponent } from './upload/upload.component';
import { ContestComponent } from './contest/contest.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'solo', component: SoloComponent },
  { path: 'versus', component: VersusComponent },
  { path: 'battle', component: BattleRoyaleComponent },
  { path: 'upload', component: UploadComponent },
  { path: 'contest', component: ContestComponent },
];
