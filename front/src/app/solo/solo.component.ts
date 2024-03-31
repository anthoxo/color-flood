import { Component, computed, HostBinding, signal } from '@angular/core';
import { RandomGridComponent } from '../components/random-grid/random-grid.component';
import { MatButtonModule } from '@angular/material/button';
import { SoloRuleComponent } from './rule/solo-rule.component';
import { SoloUploaderComponent } from './uploader/solo-uploader.component';
import { CodeHttpService } from '../services/code-http.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ErrorUtilsService } from '../services/error-utils.service';

@Component({
  selector: 'solo',
  templateUrl: './solo.component.html',
  standalone: true,
  imports: [
    RandomGridComponent,
    MatButtonModule,
    SoloRuleComponent,
    SoloUploaderComponent,
  ]
})
export class SoloComponent {
  @HostBinding('class.w-full') wFull = true;
  @HostBinding('class.h-full') hFull = true;

  file = signal<File | undefined>(undefined);
  existFile = computed(() => this.file() !== undefined);

  constructor(private codeHttpService: CodeHttpService, private errorUtilsService: ErrorUtilsService) {}

  runCode(): void {
    const file = this.file();
    if (file !== undefined) {
      this.codeHttpService.publishCodeForSolo(file)
        .subscribe({
        error: ({ error }) => this.errorUtilsService.displayAsSnack(error)
      });
    }
  }
}