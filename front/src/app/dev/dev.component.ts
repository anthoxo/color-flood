import { Component } from '@angular/core';
import { GridComponent, GridConfig } from '../components/grid/grid.component';
import { GridUtilsService } from '../services/grid-utils.service';

@Component({
  selector: 'dev',
  templateUrl: 'dev.component.html',
  styleUrl: 'dev.component.scss',
  standalone: true,
  imports: [
    GridComponent
  ]
})
export class DevComponent {

  n = 20;

  gridConfig: GridConfig = {
    colors: this.gridUtilsService.generateColors(this.n)
  };

  constructor(private gridUtilsService: GridUtilsService) {}
}