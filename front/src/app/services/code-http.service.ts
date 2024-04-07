import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { GridResultDto } from '../models/grid.model';

@Injectable({ providedIn: 'root' })
export class CodeHttpService {
  constructor(private http: HttpClient) {}

  pushCodeForSolo(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<GridResultDto>('http://localhost:8080/api/codes/solo', formData);
  }

  pushCodeForVersus(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<GridResultDto>('http://localhost:8080/api/codes/versus', formData);
  }

  pushCodeForBattleRoyale(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<GridResultDto>('http://localhost:8080/api/codes/battle', formData);
  }

  pushCode(file: File, name: string, code: number) {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('name', name);
    formData.append('code', code.toString());
    return this.http.post<{ message: string }>('http://localhost:8080/api/codes', formData);
  }
}