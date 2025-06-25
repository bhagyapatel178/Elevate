import { Component } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  credentials= {
    username:'',
    password:'',
  }
  showPassword = false;

  constructor(private http: HttpClient) {
  }

  onSubmit(){
    this.http.post<{token: string}>('/api/users/login', this.credentials).subscribe({
      next: (res: any) => {
        localStorage.setItem('token', res.token)
        alert('Login Successful')
      },
      error: err => alert('Login failed')
    })
  }

}
