import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({providedIn: 'root'})
export class ErrorUtilsService {
  constructor(private snackBar: MatSnackBar) {}

  public displayAsSnack(errorMessage: string) {
    this.snackBar.open(errorMessage, undefined, { duration: 3000, horizontalPosition: 'center', politeness: 'polite' })
  }
}