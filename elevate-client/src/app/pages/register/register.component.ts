import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import { Router } from '@angular/router';
import {NgIf} from '@angular/common';

declare const google: any;
@Component({
  selector: 'app-register',
  imports: [FormsModule,NgIf],
  standalone: true,
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent implements OnInit{

  formData= {
    username:'',
    password:'',
    email:''
  }

  showPassword = false;

  constructor(private http: HttpClient, private router: Router) {
  }

  ngOnInit(){
    google.accounts.id.initialize({
      client_id: "162014496609-g3sutimbg134rcaonprpi8qoaeqddq06.apps.googleusercontent.com",
      callback: (response:any) => this.handleCredentialResponse(response)
    })

    google.accounts.id.renderButton(document.getElementById('googleBtn'),  {
      type: "standard",
      size: "large",
      shape: "rectangular",
      text: "continue_with"
    })
  }

  onSubmit(){
    this.http.post('/api/users/register', this.formData).subscribe({
      next: (res: any) => {
        // alert(res.message);
        this.router.navigate(['/login']);
      },
      error: err => alert('Error registering user')
    })
  }


  handleCredentialResponse(response:any){
    this.http.post<{token: string}>('/api/users/auth/google', {idToken  : response.credential}).subscribe({
      next: (res: any) => {
        localStorage.setItem('token', res.token)
        this.router.navigate(['']);
        // alert('Login Successful')
      },
      error: err => alert('Login failed')
    })
  }

}
