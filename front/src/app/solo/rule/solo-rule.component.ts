import { Component } from '@angular/core';
import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'solo-rule',
  templateUrl: 'solo-rule.component.html',
  standalone: true,
  imports: [
    MatListModule,
    MatCardModule,
  ]
})
export class SoloRuleComponent {}