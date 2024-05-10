import { Component, HostBinding, signal } from '@angular/core';
import { GameRunHttpService } from '../services/game-run-http.service';
import { MatButtonModule } from '@angular/material/button';
import { ContestSelectComponent } from './select/contest-select.component';
import { GridResultDto } from '../models/grid.model';
import { ContestLadderComponent } from './ladder/contest-ladder.component';
import { MatDialog } from '@angular/material/dialog';
import { ContestDialogComponent } from './dialog/contest-dialog.component';
import { MatSlideToggle } from '@angular/material/slide-toggle';

@Component({
  selector: 'contest',
  templateUrl: './contest.component.html',
  standalone: true,
  imports: [
    MatButtonModule,
    MatSlideToggle,
    ContestSelectComponent,
    ContestLadderComponent
  ]
})
export class ContestComponent {
  @HostBinding('class.w-full') wFull = true;
  @HostBinding('class.h-full') hFull = true;

  pollChecked = signal(false);
  gridResultDto = signal<GridResultDto | undefined>(undefined);

  constructor(private gameRunHttpService: GameRunHttpService, private dialog: MatDialog) {
  }

  runContest(mode: 'SOLO' | 'VERSUS' | 'BATTLE') {
    if (mode === 'SOLO') {
      this.gameRunHttpService.runSoloContest().subscribe();
    }
    if (mode === 'VERSUS') {
      this.gameRunHttpService.runVersusContest().subscribe();
    }
    if (mode === 'BATTLE') {
      this.gameRunHttpService.runBattleContest().subscribe();
    }
  }

  onSelectGame(data: { gameId: number; mode: 'SOLO' | 'VERSUS' | 'BATTLE' }) {
    this.dialog.open(ContestDialogComponent, {
      data,
      width: '90vh',
    });
  }
}