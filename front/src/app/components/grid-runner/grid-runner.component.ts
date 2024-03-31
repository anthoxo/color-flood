import { Component, computed, DestroyRef, input, signal } from '@angular/core';
import { GridResultDto } from '../../models/grid.model';
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
  turnNumber = computed(() => this.history().length - 1);

  index = signal(0);
  grid = computed(() => {
    return this.history()[this.index()];
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