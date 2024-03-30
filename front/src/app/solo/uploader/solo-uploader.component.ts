import { Component, computed, EventEmitter, Output, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'solo-uploader',
  templateUrl: 'solo-uploader.component.html',
  standalone: true,
  imports: [
    MatButtonModule,
    MatIconModule
  ]
})
export class SoloUploaderComponent {

  @Output() onFileChange = new EventEmitter<File>();

  file = signal<File | undefined>(undefined);
  isUploaded = computed(() => this.file() !== undefined);
  fileName = computed(() => this.file()?.name ?? '');

  onFileSelected(eventTarget: any) {
    const file: File | undefined = eventTarget.files?.[0];
    this.file.set(file);
    if (file !== undefined) {
      this.onFileChange.emit(file);
    }
  }
}