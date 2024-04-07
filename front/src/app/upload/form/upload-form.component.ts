import { Component, computed, input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { UploadForm } from '../upload.component';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatList, MatListItem, MatListItemLine, MatListItemTitle } from '@angular/material/list';

@Component({
  selector: 'upload-form',
  templateUrl: 'upload-form.component.html',
  standalone: true,
  imports: [
    MatCardModule,
    MatInputModule,
    MatList,
    MatListItem,
    MatListItemLine,
    MatListItemTitle,
    ReactiveFormsModule
  ]
})
export class UploadFormComponent {
  form = input.required<FormGroup<UploadForm>>();

  nameControl = computed(() => this.form().controls.name);
  codeControl = computed(() => this.form().controls.code);
}