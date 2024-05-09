import { Component, HostBinding, signal, Signal } from '@angular/core';
import { GameRunHttpService } from '../services/game-run-http.service';
import { MatButtonModule } from '@angular/material/button';
import { toSignal } from '@angular/core/rxjs-interop';
import { GameHttpService } from '../services/game-http.service';
import { GameOverviewDto } from '../models/game-overview.model';
import { ContestSelectComponent } from './select/contest-select.component';
import { GridResultDto } from '../models/grid.model';
import { ContestLadderComponent } from './ladder/contest-ladder.component';
import { MatDialog } from '@angular/material/dialog';
import { ContestDialogComponent } from './dialog/contest-dialog.component';

@Component({
  selector: 'contest',
  templateUrl: './contest.component.html',
  standalone: true,
  imports: [
    MatButtonModule,
    ContestSelectComponent,
    ContestLadderComponent
  ]
})
export class ContestComponent {
  @HostBinding('class.w-full') wFull = true;
  @HostBinding('class.h-full') hFull = true;

  gameOverview: Signal<GameOverviewDto> = toSignal(this.gameHttpService.getAll(), {
    initialValue: {
      soloGames: [],
      versusGames: [],
      battleGames: []
    }
  });
  gridResultDto = signal<GridResultDto | undefined>(undefined);

  constructor(private gameRunHttpService: GameRunHttpService,
              private gameHttpService: GameHttpService,
              private dialog: MatDialog) {
  }

  runContest() {
    this.gameRunHttpService.runContest().subscribe(() => console.log('yes!'));
  }

  onSelectGame(data: { gameId: number; mode: 'SOLO' | 'VERSUS' | 'BATTLE' }) {
    this.dialog.open(ContestDialogComponent, {
      data,
      width: '90vh',
    });
  }
}