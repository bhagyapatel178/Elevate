import { Component } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import { Router } from '@angular/router';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-register',
  imports: [FormsModule,NgIf],
  standalone: true,
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  formData= {
    username:'',
    password:'',
    email:''
  }

  showPassword = false;

  constructor(private http: HttpClient, private router: Router) {
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

}
