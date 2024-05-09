import { Component, computed, input, output, signal, Signal, WritableSignal } from '@angular/core';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { GameOverviewDto } from '../../models/game-overview.model';
import { DecimalPipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';

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
  overview = input.required<GameOverviewDto>();
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

  select(gameId: number) {
    this.selectedGame.set(gameId);
    const mode = this.mode();
    if (mode !== undefined) {
      this.selectGame.emit({ gameId, mode });
    }
  }
}