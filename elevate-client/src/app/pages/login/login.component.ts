import {AfterViewInit, Component, OnInit,} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';

declare const google: any;
@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements AfterViewInit{
  credentials= {
    username:'',
    password:'',
  }
  showPassword = false;


  constructor(private http: HttpClient,private router: Router) {
  }

  ngAfterViewInit(){
    google.accounts.id.initialize({
      client_id: "162014496609-g3sutimbg134rcaonprpi8qoaeqddq06.apps.googleusercontent.com",
      callback: (response:any) => this.handleCredentialResponse(response),
      auto_select: false
    })

    google.accounts.id.renderButton(document.getElementById('googleBtn'),  {
      type: "standard",
      size: "medium",
      shape: "rectangular",
      text: "continue_with",
      theme: "outline",
      width: "300"
    })
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
