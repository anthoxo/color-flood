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

  runBattleRoyale(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<GridResultDto>('http://localhost:8080/api/game/run/battle', formData);
  }
}