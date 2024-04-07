import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';

@Component({
  selector: 'upload-description',
  templateUrl: 'upload-description.component.html',
  standalone: true,
  imports: [MatCardModule, MatListModule]
})
export class UploadDescriptionComponent {}