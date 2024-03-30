import { Component, HostBinding } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { GridUtilsService } from '../services/grid-utils.service';
import { GridComponent, GridConfig } from '../components/grid/grid.component';
import { MatList, MatListItem, MatListItemLine, MatListItemTitle } from '@angular/material/list';

@Component({
  selector: 'home',
  templateUrl: './home.component.html',
  standalone: true,
  imports: [
    MatCardModule,
    GridComponent,
    MatList,
    MatListItem,
    MatListItemLine,
    MatListItemTitle,
  ]
})
export class HomeComponent {
  @HostBinding("class.w-full") wFull = true;
  @HostBinding("class.h-full") hFull = true;

  gridConfigExample: GridConfig = {
    colors: this.gridUtilsService.generateColors(5)
  };

  constructor(private gridUtilsService: GridUtilsService) {}
}