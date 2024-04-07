import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class CodeHttpService {
  constructor(private http: HttpClient) {}

  pushCode(file: File, name: string, code: number) {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('name', name);
    formData.append('code', code.toString());
    return this.http.post<{ message: string }>('http://localhost:8080/api/codes', formData);
  }
}