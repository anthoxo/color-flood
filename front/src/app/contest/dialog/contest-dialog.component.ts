import { Component, computed, Inject, Signal } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { GridResultDto } from '../../models/grid.model';
import { toSignal } from '@angular/core/rxjs-interop';
import { GameHttpService } from '../../services/game-http.service';
import { GridRunnerComponent } from '../../components/grid-runner/grid-runner.component';
import { GridRunnerStatisticsComponent } from '../../components/grid-runner-statistics/grid-runner-statistics.component';

@Component({
  selector: 'contest-dialog',
  templateUrl: 'contest-dialog.component.html',
  standalone: true,
  imports: [
    MatDialogModule,
    GridRunnerComponent,
    GridRunnerStatisticsComponent
  ]
})
export class ContestDialogComponent {

  gridResultDto: Signal<GridResultDto | undefined>;

  title = computed(() => {
    const gridResultDto = this.gridResultDto();
    if (gridResultDto === undefined) {
      return '';
    }
    const statistics = gridResultDto.statistics;
    const players = [
      statistics.find(stat => stat.startingTile === 'TOP_LEFT'),
      statistics.find(stat => stat.startingTile === 'BOTTOM_RIGHT'),
      statistics.find(stat => stat.startingTile === 'TOP_RIGHT'),
      statistics.find(stat => stat.startingTile === 'BOTTOM_LEFT'),
    ]
      .map(s => s?.name)
      .filter(v => Boolean(v))
      .join(' ⚡️ ');
    return players;
  })

  constructor(
    public dialogRef: MatDialogRef<ContestDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { gameId: number; mode: 'SOLO' | 'VERSUS' | 'BATTLE' },
    private gameHttpService: GameHttpService,
  ) {
    this.gridResultDto = toSignal(this.gameHttpService.getDetail(this.data.mode, this.data.gameId))
  }
}