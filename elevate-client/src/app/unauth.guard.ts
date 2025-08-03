import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from './services/auth.service';


export const guestGuard: CanActivateFn = (_route, _state) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  if (!auth.isLoggedin()) {
    return true;
  }
  return router.createUrlTree(['/home']); //if logged in, send direct to /home
};
