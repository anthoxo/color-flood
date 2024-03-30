import { Component, OnInit } from '@angular/core';
import { GridComponent, GridConfig } from '../components/grid/grid.component';

@Component({
  selector: 'dev',
  templateUrl: 'dev.component.html',
  styleUrl: 'dev.component.scss',
  standalone: true,
  imports: [
    GridComponent
  ]
})
export class DevComponent implements OnInit {

  n = 20;

  gridConfig!: GridConfig;

  ngOnInit() {
    const array: number[][] = [];
    for (let i = 0; i < this.n; ++i) {
      array.push([]);
      for (let j = 0; j < this.n; ++j) {
        array[i].push(Math.floor(Math.random() * 20));
      }
    }

    this.gridConfig = {
      colors: array
    };
  }
}