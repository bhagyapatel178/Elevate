import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() { }

  isLoggedin(): boolean{
    return !!localStorage.getItem('token');
  }

  logout(): void{
    localStorage.removeItem('token')
  }


}
