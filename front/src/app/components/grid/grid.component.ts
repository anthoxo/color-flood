import { Component, input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { SquareComponent } from '../square/square.component';

export interface GridConfig {
  colors: number[][];
}

@Component({
  selector: 'grid',
  templateUrl: './grid.component.html',
  standalone: true,
  imports: [
    MatCardModule,
    SquareComponent
  ]
})
export class GridComponent {
  config = input.required<GridConfig>();
}