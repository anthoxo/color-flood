import { Component, computed, HostBinding, OnInit, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { UploaderComponent } from '../components/uploader/uploader.component';
import { UploadDescriptionComponent } from './description/upload-description.component';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { UploadFormComponent } from './form/upload-form.component';
import { CodeHttpService } from '../services/code-http.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ErrorUtilsService } from '../services/error-utils.service';

export interface UploadForm {
  name: FormControl<string | null>,
  code: FormControl<number | null>
}

@Component({
  selector: 'upload',
  templateUrl: 'upload.component.html',
  standalone: true,
  imports: [
    MatButtonModule,
    UploaderComponent,
    UploadDescriptionComponent,
    UploadFormComponent
  ]
})
export class UploadComponent implements OnInit {
  @HostBinding('class.w-full') wFull = true;
  @HostBinding('class.h-full') hFull = true;

  file = signal<File | undefined>(undefined);
  existFile = computed(() => this.file() !== undefined);

  form!: FormGroup;

  constructor(private fb: FormBuilder,
              private codeHttpService: CodeHttpService,
              private errorUtilsService: ErrorUtilsService,
              private snackBar: MatSnackBar) {}

  ngOnInit() {
    this.form = this.fb.group<UploadForm>({
      name: this.fb.control('', [
        Validators.required
      ]),
      code: this.fb.control(null, [
        Validators.required,
        Validators.pattern(`^\\d{4}$`)
      ]),
    });
  }

  deployCode() {
    if (this.form.invalid) {
      return;
    }

    const file = this.file();
    if (file === undefined) {
      return;
    }

    this.codeHttpService.pushCode(file, this.form.controls['name'].value ?? '', this.form.controls['code'].value)
      .subscribe({
        next: (message) => {
          this.snackBar.open(message, undefined, { duration: 5000 });
        },
        error: ({ error }) => {
          this.errorUtilsService.displayAsSnack(error);
        },
      });
  }

  resetFile() {
    this.file.set(undefined);
  }
}