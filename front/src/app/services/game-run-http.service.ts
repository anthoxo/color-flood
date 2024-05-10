import { Injectable } from '@angular/core';
import { GridResultDto } from '../models/grid.model';
import { HttpClient } from '@angular/common/http';

@Injectable({providedIn: 'root'})
export class GameRunHttpService {
  constructor(private http: HttpClient) {}

  runSolo(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<GridResultDto>('http://localhost:8080/api/game/run/solo', formData);
  }

  runVersus(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<GridResultDto>('http://localhost:8080/api/game/run/versus', formData);
  }

  runBattle(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<GridResultDto>('http://localhost:8080/api/game/run/battle', formData);
  }

  runSoloContest() {
    return this.http.post<void>('http://localhost:8080/api/game/run/solo/contest', null);
  }

  runVersusContest() {
    return this.http.post<void>('http://localhost:8080/api/game/run/versus/contest', null);
  }

  runBattleContest() {
    return this.http.post<void>('http://localhost:8080/api/game/run/battle/contest', null);
  }
}