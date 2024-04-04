import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';

@Component({
  selector: 'versus-rule',
  templateUrl: 'versus-rule.component.html',
  standalone: true,
  imports: [
    MatCardModule,
    MatListModule,
  ]
})
export class VersusRuleComponent {

}