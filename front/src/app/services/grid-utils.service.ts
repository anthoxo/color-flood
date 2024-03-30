import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class GridUtilsService {

  public generateColors(size: number): number[][] {
    const colors: number[][] = [];
    for (let i = 0; i < size; ++i) {
      colors.push([]);
      for (let j = 0; j < size; ++j) {
        colors[i].push(Math.floor(Math.random() * 20));
      }
    }
    return colors;
  }
}