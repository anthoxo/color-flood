import { Component, computed, HostBinding, signal } from '@angular/core';
import { RandomGridComponent } from '../components/random-grid/random-grid.component';
import { MatButtonModule } from '@angular/material/button';
import { SoloRuleComponent } from './rule/solo-rule.component';
import { SoloUploaderComponent } from './uploader/solo-uploader.component';
import { CodeHttpService } from '../services/code-http.service';
import { ErrorUtilsService } from '../services/error-utils.service';
import { GridResultDto } from '../models/grid.model';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { GridRunnerComponent } from '../components/grid-runner/grid-runner.component';

@Component({
  selector: 'solo',
  templateUrl: './solo.component.html',
  standalone: true,
  imports: [
    RandomGridComponent,
    MatButtonModule,
    SoloRuleComponent,
    SoloUploaderComponent,
    MatProgressSpinner,
    GridRunnerComponent,
  ]
})
export class SoloComponent {
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
      this.codeHttpService.publishCodeForSolo(file)
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