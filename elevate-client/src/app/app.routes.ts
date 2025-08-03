import { Routes } from '@angular/router';
import {authGuard} from './auth.guard';
import {guestGuard} from './unauth.guard';

export const routes: Routes = [
  { path: '', loadComponent:()=> import('../app/pages/landing.component').then(m=> m.LandingComponent), canActivate: [guestGuard]},
  { path: 'register', loadComponent:()=> import('../app/pages/register/register.component').then(m=> m.RegisterComponent), canActivate: [guestGuard]},
  { path: 'login', loadComponent:()=> import('../app/pages/login/login.component').then(m=> m.LoginComponent), canActivate: [guestGuard]},
  { path: 'home', loadComponent:()=> import('../app/home/home.component').then(m=> m.HomeComponent), canActivate: [authGuard]},
  { path: 'profile', loadComponent:()=> import('../app/pages/profile/profile.component').then(m=> m.ProfileComponent), canActivate: [authGuard]},
  { path: 'friends', loadComponent:()=> import('../app/pages/friends/friends.component').then(m=> m.FriendsComponent), canActivate: [authGuard]},

  //nav bar home button routes to root /, so if not logged in, goes to landing, if logged in it routes to root but redirects to /home

  { path: '**', redirectTo:''},
];
