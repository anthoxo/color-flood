import { Component, computed, effect, HostBinding, signal, Signal, WritableSignal } from '@angular/core';
import { GameRunHttpService } from '../services/game-run-http.service';
import { MatButton } from '@angular/material/button';
import { toSignal } from '@angular/core/rxjs-interop';
import { LadderHttpService } from '../services/ladder-http.service';
import { UserDto } from '../models/user.model';
import { MatCard, MatCardContent, MatCardHeader, MatCardTitle } from '@angular/material/card';
import { MatList, MatListItem, MatListItemLine, MatListItemTitle } from '@angular/material/list';
import { DecimalPipe } from '@angular/common';
import { GameHttpService } from '../services/game-http.service';
import { GameOverviewDto } from '../models/game-overview.model';
import { GridRunnerComponent } from '../components/grid-runner/grid-runner.component';
import { GridRunnerStatisticsComponent } from '../components/grid-runner-statistics/grid-runner-statistics.component';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { RandomGridComponent } from '../components/random-grid/random-grid.component';
import { UploaderComponent } from '../components/uploader/uploader.component';
import { VersusRuleComponent } from '../versus/rule/versus-rule.component';
import { ContestSelectComponent } from './select/contest-select.component';
import { GridResultDto } from '../models/grid.model';

@Component({
  selector: 'contest',
  templateUrl: './contest.component.html',
  standalone: true,
  imports: [
    MatButton,
    MatCard,
    MatCardContent,
    MatCardHeader,
    MatCardTitle,
    MatList,
    MatListItem,
    MatListItemLine,
    MatListItemTitle,
    DecimalPipe,
    GridRunnerComponent,
    GridRunnerStatisticsComponent,
    MatProgressSpinner,
    RandomGridComponent,
    UploaderComponent,
    VersusRuleComponent,
    ContestSelectComponent
  ]
})
export class ContestComponent {
  @HostBinding('class.w-full') wFull = true;
  @HostBinding('class.h-full') hFull = true;

  userDtos: Signal<UserDto[]> = toSignal(this.ladderHttpService.getLadder(), { initialValue: [] });
  sortedUserDtos = computed(() => {
    const userDtos = this.userDtos();
    userDtos.sort((userA, userB) => userB.elo - userA.elo);
    return userDtos;
  })

  gameOverview: Signal<GameOverviewDto> = toSignal(this.gameHttpService.getAll(), {
    initialValue: {
      soloGames: [],
      versusGames: [],
      battleGames: []
    }
  });
  loading = signal<boolean>(false);
  gridResultDto = signal<GridResultDto | undefined>(undefined);

  constructor(private gameRunHttpService: GameRunHttpService,
              private ladderHttpService: LadderHttpService,
              private gameHttpService: GameHttpService) {
  }

  getLadderIcon(user: UserDto) {
    const sortedUsers = this.sortedUserDtos();
    if (user.id === sortedUsers[0].id) {
      return '🥇';
    }
    if (user.id === sortedUsers[1].id) {
      return '🥈';
    }
    if (user.id === sortedUsers[2].id) {
      return '🥉';
    }
    return '🍫';
  }

  runContest() {
    this.gameRunHttpService.runContest().subscribe(() => console.log('yes!'));
  }

  onSelectGame(choice: { gameId: number; mode: 'SOLO' | 'VERSUS' | 'BATTLE' }) {
    this.loading.set(true);
    this.gameHttpService.getDetail(choice.mode, choice.gameId)
      .subscribe({
        next: (gridResultDto) => {
          this.gridResultDto.set(gridResultDto);
          this.loading.set(false);
        },
        error: (e) => {
          console.log('error', e);
          this.gridResultDto.set(undefined);
          this.loading.set(false);
        }
      });
  }
}