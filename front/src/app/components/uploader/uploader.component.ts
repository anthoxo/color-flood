import { Component, computed, EventEmitter, input, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'uploader',
  templateUrl: 'uploader.component.html',
  standalone: true,
  imports: [
    MatButtonModule,
    MatIconModule
  ]
})
export class UploaderComponent {
  file = input.required<File | undefined>();
  @Output() onFileChange = new EventEmitter<File | undefined>();

  isUploaded = computed(() => this.file() !== undefined);
  fileName = computed(() => this.file()?.name ?? '');

  onFileSelected(eventTarget: any) {
    const file: File | undefined = eventTarget.files?.[0];
    if (file !== undefined) {
      this.onFileChange.emit(file);
    }
    // to allow putting the same file twice
    eventTarget.value = null;
  }
}