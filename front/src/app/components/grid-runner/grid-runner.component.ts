import { Component, computed, DestroyRef, input, Signal, signal } from '@angular/core';
import { GridResultDto, StartingTile } from '../../models/grid.model';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { interval, Subscription, take } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { GridComponent } from '../grid/grid.component';
import { MatSliderModule } from '@angular/material/slider';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'grid-runner',
  templateUrl: 'grid-runner.component.html',
  styleUrl: 'grid-runner.component.scss',
  standalone: true,
  imports: [
    AsyncPipe,
    GridComponent,
    MatSliderModule,
    MatButtonModule,
    MatIconModule,
  ]
})
export class GridRunnerComponent {
  gridResultDto = input<GridResultDto>();

  history = computed(() => {
    const gridResultDto = this.gridResultDto();
    if (gridResultDto !== undefined) {
      return gridResultDto.history;
    }
    return [];
  });
  nbOfPlayers: Signal<number> = computed(() => {
    const gridResultDto = this.gridResultDto();
    if (gridResultDto !== undefined) {
      return gridResultDto.statistics.length;
    }
    return 1;
  });

  turnNumber = computed(() => this.history().length - 1);

  index = signal(0);

  enrichedGrid = computed(() => {
    return this.history()[this.index()];
  });
  grid = computed(() => {
    return this.enrichedGrid().grid;
  });
  usedJoker = computed(() => {
    return this.enrichedGrid().usedJoker;
  });

  startingTile: Signal<StartingTile> = computed(() => {
    const tiles: StartingTile[] = ['TOP_LEFT', 'BOTTOM_RIGHT', 'TOP_RIGHT', 'BOTTOM_LEFT'];
    return tiles[(this.index() - 1) % this.nbOfPlayers()]
  });

  currentSubscription = signal<Subscription | undefined>(undefined);
  isRunning = computed(() => this.currentSubscription() !== undefined);

  constructor(private destroyRef: DestroyRef) {}

  run() {
    this.currentSubscription.set(interval(100)
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        take(this.turnNumber() - this.index()),
      ).subscribe({
        next: () => this.goNext(),
        complete: () => this.stop()
      }));
  }

  stop() {
    this.currentSubscription()?.unsubscribe();
    this.currentSubscription.set(undefined);
  }

  goPrevious() {
    const index = this.index();
    if (index > 0) {
      this.index.set(index - 1);
    }
  }

  goNext() {
    const index = this.index();
    if (index < this.turnNumber()) {
      this.index.set(index + 1);
    }
  }
}