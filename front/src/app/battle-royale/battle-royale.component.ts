import { Component, computed, HostBinding, signal } from '@angular/core';
import { GridRunnerComponent } from '../components/grid-runner/grid-runner.component';
import { GridRunnerStatisticsComponent } from '../components/grid-runner-statistics/grid-runner-statistics.component';
import { MatButton } from '@angular/material/button';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { RandomGridComponent } from '../components/random-grid/random-grid.component';
import { UploaderComponent } from '../components/uploader/uploader.component';
import { GridResultDto } from '../models/grid.model';
import { ErrorUtilsService } from '../services/error-utils.service';
import { BattleRoyaleRuleComponent } from './rule/battle-royale-rule.component';
import { GameRunHttpService } from '../services/game-run-http.service';

@Component({
  selector: 'battle-royale',
  templateUrl: 'battle-royale.component.html',
  standalone: true,
  imports: [
    GridRunnerComponent,
    GridRunnerStatisticsComponent,
    MatButton,
    MatProgressSpinner,
    RandomGridComponent,
    BattleRoyaleRuleComponent,
    UploaderComponent
  ]
})
export class BattleRoyaleComponent {
  @HostBinding('class.w-full') wFull = true;
  @HostBinding('class.h-full') hFull = true;

  file = signal<File | undefined>(undefined);
  existFile = computed(() => this.file() !== undefined);

  loading = signal<boolean>(false);
  gridResultDto = signal<GridResultDto | undefined>(undefined);

  constructor(private gameRunHttpService: GameRunHttpService, private errorUtilsService: ErrorUtilsService) {}

  runCode(): void {
    const file = this.file();
    if (file !== undefined) {
      this.loading.set(true);
      this.gameRunHttpService.runBattle(file)
        .subscribe({
          next: (dto) => {
            this.gridResultDto.set(dto);
            this.loading.set(false);
          },
          error: ({ error }) => {
            this.errorUtilsService.displayAsSnack(error);
            this.loading.set(false);
          },
        });
    }
  }

  resetFile() {
    this.file.set(undefined);
  }
}