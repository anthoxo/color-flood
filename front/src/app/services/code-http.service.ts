import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { GridResultDto } from '../models/grid.model';

@Injectable({ providedIn: 'root'})
export class CodeHttpService {
  constructor(private http: HttpClient) {}

  publishCodeForSolo(file: File) {
    const formData = new FormData();
    formData.append("file", file);
    return this.http.post<GridResultDto>("http://localhost:8080/api/codes/solo", formData);
  }
}