import { Component, computed, input, signal, Signal, ViewChild } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { UserDto } from '../../models/user.model';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { LadderHttpService } from '../../services/ladder-http.service';
import { DecimalPipe } from '@angular/common';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatCardModule } from '@angular/material/card';
import { interval, switchMap } from 'rxjs';

@Component({
  selector: 'contest-ladder',
  templateUrl: 'contest-ladder.component.html',
  standalone: true,
  imports: [
    MatCardModule,
    MatTableModule,
    MatSortModule,
    DecimalPipe
  ]
})
export class ContestLadderComponent {
  @ViewChild(MatSort) set matSort(sort: MatSort) {
    this.sortSignal.set(sort);
  }

  poll = input.required<boolean>();

  userDtos$ = toObservable(this.poll)
    .pipe(
      switchMap((shouldPoll) => {
        if (shouldPoll) {
          return interval(2000)
            .pipe(
              switchMap(() => this.ladderHttpService.getLadder())
            );
        }
        return this.ladderHttpService.getLadder();
      })
    );

  userDtos: Signal<UserDto[]> = toSignal(this.userDtos$, { initialValue: [] });
  sortedUserDtos = computed(() => {
    const userDtos = this.userDtos();
    userDtos.sort((userA, userB) => userB.elo - userA.elo);
    return userDtos;
  });

  sortSignal = signal<MatSort | undefined>(undefined);

  datasource = computed(() => {
    const datasource = new MatTableDataSource(this.sortedUserDtos());
    const sort = this.sortSignal();
    if (sort !== undefined) {
      datasource.sort = sort;
    }
    return datasource;
  });

  displayedColumns = ['position', 'teamName', 'elo', 'soloElo', 'versusElo', 'battleElo'];

  constructor(private ladderHttpService: LadderHttpService) {}

  getLadderIcon(index: number) {
    if (index == 1) {
      return '🥇';
    }
    if (index == 2) {
      return '🥈';
    }
    if (index == 3) {
      return '🥉';
    }
    return '🍫';
  }
}