import { Component } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Router } from '@angular/router';
import { MatButton } from '@angular/material/button';

@Component({
  selector: 'toolbar',
  templateUrl: './toolbar.component.html',
  standalone: true,
  imports: [MatToolbarModule, MatButton]
})
export class ToolbarComponent {
  constructor(private router: Router) {}

  goToDev() {
    this.router.navigate(['/dev']);
  }

  goToHome() {
    this.router.navigate(['']);
  }

  goToSolo() {
    this.router.navigate(['/solo']);
  }

  goToLadder() {
    this.router.navigate(['/ladder']);
  }
}