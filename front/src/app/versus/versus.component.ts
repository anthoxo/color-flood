import { Component, computed, HostBinding, signal } from '@angular/core';
import { GridRunnerComponent } from '../components/grid-runner/grid-runner.component';
import { GridRunnerStatisticsComponent } from '../components/grid-runner-statistics/grid-runner-statistics.component';
import { MatButton } from '@angular/material/button';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { RandomGridComponent } from '../components/random-grid/random-grid.component';
import { UploaderComponent } from '../components/uploader/uploader.component';
import { GridResultDto } from '../models/grid.model';
import { CodeHttpService } from '../services/code-http.service';
import { ErrorUtilsService } from '../services/error-utils.service';
import { VersusRuleComponent } from './rule/versus-rule.component';

@Component({
  selector: 'versus',
  templateUrl: 'versus.component.html',
  standalone: true,
  imports: [
    GridRunnerComponent,
    GridRunnerStatisticsComponent,
    MatButton,
    MatProgressSpinner,
    RandomGridComponent,
    VersusRuleComponent,
    UploaderComponent
  ]
})
export class VersusComponent {
  @HostBinding('class.w-full') wFull = true;
  @HostBinding('class.h-full') hFull = true;

  file = signal<File | undefined>(undefined);
  existFile = computed(() => this.file() !== undefined);

  loading = signal<boolean>(false);
  gridResultDto = signal<GridResultDto | undefined>(undefined);

  constructor(private codeHttpService: CodeHttpService, private errorUtilsService: ErrorUtilsService) {}

  runCode(): void {
    const file = this.file();
    if (file !== undefined) {
      this.loading.set(true);
      this.codeHttpService.pushCodeForVersus(file)
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