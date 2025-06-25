import { Component } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';

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

  constructor(private http: HttpClient,private router: Router) {
  }

  onSubmit(){
    this.http.post<{token: string}>('/api/users/login', this.credentials).subscribe({
      next: (res: any) => {
        localStorage.setItem('token', res.token)
        this.router.navigate(['']);
        // alert('Login Successful')
      },
      error: err => alert('Login failed')
    })
  }

}
