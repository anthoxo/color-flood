import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';

@Component({
  selector: 'battle-royale-rule',
  templateUrl: 'battle-royale-rule.component.html',
  standalone: true,
  imports: [
    MatCardModule,
    MatListModule,
  ]
})
export class BattleRoyaleRuleComponent {

}