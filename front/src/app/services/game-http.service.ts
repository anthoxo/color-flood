import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { GameOverviewDto } from '../models/game-overview.model';
import { GridResultDto } from '../models/grid.model';
import { of } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class GameHttpService {
  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<GameOverviewDto>('http://localhost:8080/api/game');
  }

  getDetail(mode: 'SOLO' | 'VERSUS' | 'BATTLE', gameId: number) {
    if (mode === 'SOLO') {
      return this.http.get<GridResultDto>(`http://localhost:8080/api/game/solo/${gameId}`);
    }
    if (mode === 'VERSUS') {
      return this.http.get<GridResultDto>(`http://localhost:8080/api/game/versus/${gameId}`);
    }
    if (mode === 'BATTLE') {
      return this.http.get<GridResultDto>(`http://localhost:8080/api/game/battle/${gameId}`);
    }
    return of();
  }
}