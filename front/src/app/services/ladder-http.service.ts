import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserDto } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class LadderHttpService {
  constructor(private http: HttpClient) {}

  public getLadder(): Observable<UserDto[]> {
    return this.http.get<UserDto[]>("http://localhost:8080/api/ladder")
  }
}