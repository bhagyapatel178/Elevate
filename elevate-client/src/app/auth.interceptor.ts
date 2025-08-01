import { HttpInterceptorFn } from '@angular/common/http';
import {AuthService} from './services/auth.service';
import {inject} from '@angular/core';
import {environment} from '../environments/environment';


export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const authService = inject(AuthService);
  const token = authService.getAuthToken();

  // Prepend the base URL if it's a relative request
  const isRelativeUrl = !/^http(s)?:\/\//.test(req.url);
  const updatedUrl = isRelativeUrl ? environment.apiBaseUrl + req.url : req.url;


  // Skip auth for login and register
  const isPublicEndpoint =
    req.url.includes('/login') || req.url.includes('/register');

  const updatedReq = token && !isPublicEndpoint
    ? req.clone({
      url: updatedUrl,
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    })
    : req.clone({
      url: updatedUrl
    });

  return next(updatedReq);
};
