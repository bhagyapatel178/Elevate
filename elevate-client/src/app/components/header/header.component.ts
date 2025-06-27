import { Component } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-header',
  imports: [RouterLink,NgIf],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  constructor(public auth: AuthService, private router: Router) {
  }

  logout(){
    this.auth.logout();
    this.router.navigate(['/login']);
  }

}
