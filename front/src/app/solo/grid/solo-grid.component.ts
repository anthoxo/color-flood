import { Component, computed, input } from '@angular/core';
import { GridResultDto } from '../../models/grid.model';
import { concatMap, delay, from, of } from 'rxjs';
import { GridComponent } from '../../components/grid/grid.component';
import { AsyncPipe } from '@angular/common';
import { toObservable } from '@angular/core/rxjs-interop';

@Component({
  selector: 'solo-grid',
  templateUrl: 'solo-grid.component.html',
  standalone: true,
  imports: [
    GridComponent,
    AsyncPipe,
  ]
})
export class SoloGridComponent {
  gridResultDto = input<GridResultDto>();
  history = computed(() => {
    const gridResultDto = this.gridResultDto();
    if (gridResultDto !== undefined) {
      return gridResultDto.history;
    }
    return []
  });

  grid$ = toObservable(this.history)
   .pipe(
     concatMap(grids => from(grids)),
     concatMap(grid => of(grid).pipe(delay(300)))
   );
}