import { Component, computed, input } from '@angular/core';
import { GridResultDto, StartingTile } from '../../models/grid.model';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';

const LABEL_BY_STARTING_TILE: Record<StartingTile, string> = {
  TOP_LEFT: 'Top left.',
  BOTTOM_RIGHT: 'Bottom right.'
}

@Component({
  selector: 'grid-runner-statistics',
  templateUrl: 'grid-runner-statistics.component.html',
  standalone: true,
  imports: [
    MatCardModule,
    MatListModule
  ]
})
export class GridRunnerStatisticsComponent {
  gridResultDto = input<GridResultDto>();
  historic = computed(() => this.gridResultDto()?.history ?? []);

  turnNumber = computed(() => this.historic().length);
  statistics = computed(() => (this.gridResultDto()?.statistics ?? []).sort((a,b) => a.rank - b.rank));

  getLabel(startingTile: StartingTile): string {
    return LABEL_BY_STARTING_TILE[startingTile];
  }
}