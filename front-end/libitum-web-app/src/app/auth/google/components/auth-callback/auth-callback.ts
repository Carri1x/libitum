import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../service/auth-service';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-auth-callback',
  imports: [
    CommonModule
  ],
  templateUrl: './auth-callback.html',
  styleUrl: './auth-callback.css'
})
export class AuthCallbackComponent implements OnInit {
  isLoading = true;
  error: string | null = null;
  success = false;
  user: any = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.handleCallback();
  }

  private handleCallback(): void {
    this.route.queryParams.subscribe(params => {
      const token = params['token'];
      const error = params['error'];
      const errorMessage = params['message'];

      if (error) {
        this.isLoading = false;
        this.error = errorMessage || 'Error durante la autenticación';
        return;
      }

      if (token) {
        this.authService.handleOAuthCallback(token, params['email'], params['name'])
          .subscribe({
            next: (response) => {
              this.isLoading = false;
              if (response.valid && response.user) {
                this.success = true;
                this.user = response.user;
                // Redirigir al dashboard después de 2 segundos
                setTimeout(() => {
                  this.router.navigate(['/dashboard']);
                }, 2000);
              } else {
                this.error = response.error || 'Token inválido';
              }
            },
            error: (err) => {
              this.isLoading = false;
              this.error = 'Error procesando la autenticación';
              console.error('Auth callback error:', err);
            }
          });
      } else {
        this.isLoading = false;
        this.error = 'No se recibió token de autenticación';
      }
    });
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }
}

// No olvides añadir la ruta en tu app-routing.module.ts:
/*
{
  path: 'auth/success',
  component: AuthCallbackComponent
},
{
  path: 'auth/error',
  component: AuthCallbackComponent
}
*/
