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

  goToHome() {
    this.router.navigate(['']);
  }

  goToSolo() {
    this.router.navigate(['/solo']);
  }

  goToVersus() {
    this.router.navigate(['/versus']);
  }

  goToBattleRoyale() {
    this.router.navigate(['/battle']);
  }

  goToUpload() {
    this.router.navigate(['/upload']);
  }

  goToContest() {
    this.router.navigate(['/contest']);
  }
}