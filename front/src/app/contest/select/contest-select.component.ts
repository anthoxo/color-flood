import { Component, computed, input, output, signal, Signal, WritableSignal } from '@angular/core';
import { MatButtonToggle, MatButtonToggleGroup } from '@angular/material/button-toggle';
import { GameOverviewDto } from '../../models/game-overview.model';
import { MatList, MatListItem, MatListItemLine, MatListItemTitle } from '@angular/material/list';
import { DecimalPipe } from '@angular/common';
import { MatButton } from '@angular/material/button';
import { MatDivider } from '@angular/material/divider';

@Component({
  selector: 'contest-select',
  templateUrl: 'contest-select.component.html',
  standalone: true,
  imports: [
    MatButtonToggleGroup,
    MatButtonToggle,
    MatList,
    DecimalPipe,
    MatButton,
    MatListItem,
    MatListItemLine,
    MatListItemTitle,
    MatDivider
  ]
})
export class ContestSelectComponent {
  overview = input.required<GameOverviewDto>();
  selectGame = output<{ gameId: number; mode: 'SOLO' | 'VERSUS' | 'BATTLE' }>();

  mode: WritableSignal<'SOLO' | 'VERSUS' | 'BATTLE' | undefined> = signal(undefined);

  games = computed(() => {
    const gameSelection = this.mode();
    if (gameSelection === "SOLO") {
      return this.overview().soloGames;
    }
    if (gameSelection === "VERSUS") {
      return this.overview().versusGames;
    }
    if (gameSelection === "BATTLE") {
      return this.overview().battleGames;
    }
    return [];
  });
  selectedGame = signal<number>(0);


  select(gameId: number) {
    this.selectedGame.set(gameId);
    const mode = this.mode();
    if (mode !== undefined) {
      this.selectGame.emit({ gameId, mode });
    }
  }
}