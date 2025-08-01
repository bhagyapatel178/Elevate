import { Routes } from '@angular/router';
import {authGuard} from './auth.guard';

export const routes: Routes = [
  { path: '', loadComponent:()=> import('../app/home/home.component').then(m=> m.HomeComponent)},
  { path: 'register', loadComponent:()=> import('../app/pages/register/register.component').then(m=> m.RegisterComponent)},
  { path: 'login', loadComponent:()=> import('../app/pages/login/login.component').then(m=> m.LoginComponent)},
  { path: 'profile', loadComponent:()=> import('../app/pages/profile/profile.component').then(m=> m.ProfileComponent), canActivate: [authGuard]},
  { path: 'friends', loadComponent:()=> import('../app/pages/friends/friends.component').then(m=> m.FriendsComponent), canActivate: [authGuard]},

];
