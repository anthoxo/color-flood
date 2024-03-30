import { Component } from '@angular/core';
import { GridComponent, GridConfig } from '../components/grid/grid.component';
import { GridUtilsService } from '../services/grid-utils.service';
import { RandomGridComponent } from '../components/random-grid/random-grid.component';

@Component({
  selector: 'dev',
  templateUrl: 'dev.component.html',
  styleUrl: 'dev.component.scss',
  standalone: true,
  imports: [
    GridComponent,
    RandomGridComponent
  ]
})
export class DevComponent {
  constructor(private gridUtilsService: GridUtilsService) {}
}