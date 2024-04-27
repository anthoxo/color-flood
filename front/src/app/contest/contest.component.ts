import { Component, HostBinding } from '@angular/core';
import { CodeHttpService } from '../services/code-http.service';
import { GameRunHttpService } from '../services/game-run-http.service';
import { MatButton } from '@angular/material/button';

@Component({
  selector: 'contest',
  templateUrl: './contest.component.html',
  standalone: true,
  imports: [
    MatButton
  ]
})
export class ContestComponent {
  @HostBinding("class.w-full") wFull = true;
  @HostBinding("class.h-full") hFull = true;

  constructor(private gameRunHttpService: GameRunHttpService) {}

  runContest() {
    this.gameRunHttpService.runContest().subscribe(() => console.log("yes!"));
  }
}