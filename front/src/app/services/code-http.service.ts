import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { GridConfig } from '../components/grid/grid.component';

@Injectable({ providedIn: 'root'})
export class CodeHttpService {
  constructor(private http: HttpClient) {}

  publishCodeForSolo(file: File) {
    const formData = new FormData();
    formData.append("file", file);
    return this.http.post<GridConfig>("http://localhost:8080/api/codes/solo", formData);
  }
}