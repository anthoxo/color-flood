import { Component, computed, signal, Signal, ViewChild } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { UserDto } from '../../models/user.model';
import { toSignal } from '@angular/core/rxjs-interop';
import { LadderHttpService } from '../../services/ladder-http.service';
import { DecimalPipe } from '@angular/common';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatCardModule } from '@angular/material/card';

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
  userDtos: Signal<UserDto[]> = toSignal(this.ladderHttpService.getLadder(), { initialValue: [] });
  sortedUserDtos = computed(() => {
    const userDtos = this.userDtos();
    userDtos.sort((userA, userB) => userB.soloElo - userA.soloElo);
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

  displayedColumns = ["position", "teamName", "soloElo", "versusElo", "battleElo"];

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