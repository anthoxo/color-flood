import { Component, computed, input, Signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { SquareComponent } from '../square/square.component';
import { Grid, Joker, StartingTile } from '../../models/grid.model';

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
  config = input.required<Grid>();
  usedJoker = input<Joker>('NONE');
  jokerPosition = input<StartingTile>('TOP_LEFT');

  gridSize = computed(() => this.config().colors.length);
  grid: Signal<{ color: number, joker: Joker }[][]> = computed(() => {
    const res: { color: number, joker: Joker }[][] = [];
    for (let i = 0; i < this.gridSize(); ++i) {
      const line: { color: number, joker: Joker }[] = [];
      for (let j = 0; j < this.gridSize(); ++j) {
        line.push({ color: this.config().colors[i][j], joker: 'NONE' });
      }
      res.push(line);
    }

    if (this.jokerPosition() == 'TOP_LEFT') {
      res[0][0] = { ...res[0][0], joker: this.usedJoker() }
    }
    if (this.jokerPosition() == 'BOTTOM_RIGHT') {
      res[this.gridSize() - 1][this.gridSize() - 1] = { ...res[this.gridSize() - 1][this.gridSize() - 1], joker: this.usedJoker() }
    }
    if (this.jokerPosition() == 'TOP_RIGHT') {
      res[0][this.gridSize() - 1] = { ...res[0][this.gridSize() - 1], joker: this.usedJoker() }
    }
    if (this.jokerPosition() == 'BOTTOM_LEFT') {
      res[this.gridSize() - 1][0] = { ...res[this.gridSize() - 1][0], joker: this.usedJoker() }
    }

    return res;
  });
}