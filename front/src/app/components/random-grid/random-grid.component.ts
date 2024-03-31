import { Component, DestroyRef } from '@angular/core';
import { GridComponent } from '../grid/grid.component';
import { GridUtilsService } from '../../services/grid-utils.service';
import { interval, map, startWith, take } from 'rxjs';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { AsyncPipe } from '@angular/common';
import { Grid } from '../../models/grid.model';

@Component({
  selector: 'random-grid',
  templateUrl: 'random-grid.component.html',
  standalone: true,
  imports: [
    GridComponent,
    AsyncPipe
  ]
})
export class RandomGridComponent {
  grids: Grid[] = [
    {
      colors: this.gridUtilsService.generateColors(20)
    },
    {
      colors: this.gridUtilsService.generateColors(20)
    },
    {
      colors: this.gridUtilsService.generateColors(20)
    },
    {
      colors: this.gridUtilsService.generateColors(20)
    },
  ];
  config$ = interval(250)
    .pipe(
      takeUntilDestroyed(this.destroyRef),
      map(val => this.grids[val % 4]),
      startWith(this.grids[2]),
      take(8)
    );

  config = toSignal(this.config$, { requireSync: true });

  constructor(private gridUtilsService: GridUtilsService, private destroyRef: DestroyRef) {}
}