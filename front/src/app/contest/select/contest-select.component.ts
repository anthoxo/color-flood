import { Component, computed, input, output, signal, WritableSignal } from '@angular/core';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { DecimalPipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { interval, switchMap } from 'rxjs';
import { GameHttpService } from '../../services/game-http.service';

@Component({
  selector: 'contest-select',
  templateUrl: 'contest-select.component.html',
  standalone: true,
  imports: [
    MatTableModule,
    MatButtonToggleModule,
    MatButtonModule,
    MatCardModule,
    DecimalPipe,
  ]
})
export class ContestSelectComponent {
  poll = input.required<boolean>();

  overview$ = toObservable(this.poll)
    .pipe(
      switchMap((shouldPoll) => {
        if (shouldPoll) {
          return interval(2000)
            .pipe(
              switchMap(() => this.gameHttpService.getAll())
            );
        }
        return this.gameHttpService.getAll();
      })
    );

  overview = toSignal(this.overview$, {
    initialValue: {
      soloGames: [],
      versusGames: [],
      battleGames: []
    }
  });
  selectGame = output<{ gameId: number; mode: 'SOLO' | 'VERSUS' | 'BATTLE' }>();

  mode: WritableSignal<'SOLO' | 'VERSUS' | 'BATTLE' | undefined> = signal(undefined);

  games = computed(() => {
    const gameSelection = this.mode();
    if (gameSelection === 'SOLO') {
      return [...this.overview().soloGames].sort((a, b) => b.id - a.id);
    }
    if (gameSelection === 'VERSUS') {
      return [...this.overview().versusGames].sort((a, b) => b.id - a.id);
    }
    if (gameSelection === 'BATTLE') {
      return [...this.overview().battleGames].sort((a, b) => b.id - a.id);
    }
    return [];
  });
  selectedGame = signal<number>(0);
  datasource = computed(() => {
    return new MatTableDataSource(this.games());
  });
  displayedColumns = ['gridId', 'players'];

  constructor(private gameHttpService: GameHttpService) {}

  select(gameId: number) {
    this.selectedGame.set(gameId);
    const mode = this.mode();
    if (mode !== undefined) {
      this.selectGame.emit({ gameId, mode });
    }
  }
}