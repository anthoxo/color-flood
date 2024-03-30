import { Component, HostBinding } from '@angular/core';
import { RandomGridComponent } from '../components/random-grid/random-grid.component';
import { MatButtonModule } from '@angular/material/button';
import { SoloRuleComponent } from './rule/solo-rule.component';
import { SoloUploaderComponent } from './uploader/solo-uploader.component';

@Component({
  selector: 'solo',
  templateUrl: './solo.component.html',
  standalone: true,
  imports: [
    RandomGridComponent,
    MatButtonModule,
    SoloRuleComponent,
    SoloUploaderComponent,
  ]
})
export class SoloComponent {
  @HostBinding('class.w-full') wFull = true;
  @HostBinding('class.h-full') hFull = true;

  existFile = false;
}