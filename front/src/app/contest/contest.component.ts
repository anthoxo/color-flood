import { Component, HostBinding, Signal } from '@angular/core';
import { CodeHttpService } from '../services/code-http.service';
import { GameRunHttpService } from '../services/game-run-http.service';
import { MatButton } from '@angular/material/button';
import { toSignal } from '@angular/core/rxjs-interop';
import { LadderHttpService } from '../services/ladder-http.service';
import { UserDto } from '../models/user.model';
import { MatCard, MatCardContent, MatCardHeader, MatCardTitle } from '@angular/material/card';
import { MatList, MatListItem, MatListItemLine, MatListItemTitle } from '@angular/material/list';

@Component({
  selector: 'contest',
  templateUrl: './contest.component.html',
  standalone: true,
  imports: [
    MatButton,
    MatCard,
    MatCardContent,
    MatCardHeader,
    MatCardTitle,
    MatList,
    MatListItem,
    MatListItemLine,
    MatListItemTitle
  ]
})
export class ContestComponent {
  @HostBinding('class.w-full') wFull = true;
  @HostBinding('class.h-full') hFull = true;

  userDtos: Signal<UserDto[]> = toSignal(this.ladderHttpService.getLadder(), { initialValue: [] });

  constructor(private gameRunHttpService: GameRunHttpService, private ladderHttpService: LadderHttpService) {}

  runContest() {
    this.gameRunHttpService.runContest().subscribe(() => console.log('yes!'));
  }
}