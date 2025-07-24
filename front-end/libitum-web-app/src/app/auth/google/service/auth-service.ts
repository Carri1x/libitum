import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';

export interface User {
  id: number;
  name: string;
  surname: string;
  nickname: string;
  description?: string;
  avatarUrl?: string;
  region?: string;
  country?: string;
}

export interface TokenValidationResponse {
  valid: boolean;
  user?: User;
  email?: string;
  expired?: boolean;
  error?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8080';
  private readonly TOKEN_KEY = 'auth_token';
  
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    // Verificar si hay un token guardado al iniciar
    this.checkStoredToken();
  }

  /**
   * Inicia el proceso de autenticación con Google
   */
  loginWithGoogle(): void {
    // Redirigir directamente al endpoint de OAuth2 del backend
    window.location.href = `${this.API_URL}/oauth2/authorization/google`;
  }

  /**
   * Maneja el callback de OAuth2 (llamar desde el componente de callback)
   */
  handleOAuthCallback(token: string, email?: string, name?: string): Observable<TokenValidationResponse> {
    if (token) {
      this.setToken(token);
      return this.validateToken(token);
    }
    throw new Error('No token received');
  }

  /**
   * Valida un token JWT
   */
  validateToken(token: string): Observable<TokenValidationResponse> {
    return new Observable(observer => {
      this.http.post<TokenValidationResponse>(`${this.API_URL}/api/auth/validate`, { token })
        .subscribe({
          next: (response) => {
            if (response.valid && response.user) {
              this.currentUserSubject.next(response.user);
              this.isAuthenticatedSubject.next(true);
            } else {
              this.logout();
            }
            observer.next(response);
            observer.complete();
          },
          error: (error) => {
            console.error('Error validating token:', error);
            this.logout();
            observer.error(error);
          }
        });
    });
  }

  /**
   * Obtiene información del usuario actual
   */
  getCurrentUser(): Observable<User> {
    const token = this.getToken();
    if (!token) {
      throw new Error('No token available');
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<User>(`${this.API_URL}/api/auth/me`, { headers });
  }

  /**
   * Cierra sesión
   */
  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.currentUserSubject.next(null);
    this.isAuthenticatedSubject.next(false);
    this.router.navigate(['/login']);
  }

  /**
   * Obtiene el token actual
   */
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  /**
   * Guarda el token
   */
  private setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  /**
   * Verifica si hay un token guardado al iniciar la aplicación
   */
  private checkStoredToken(): void {
    const token = this.getToken();
    if (token) {
      this.validateToken(token).subscribe({
        next: (response) => {
          if (!response.valid) {
            this.logout();
          }
        },
        error: () => {
          this.logout();
        }
      });
    }
  }

  /**
   * Verifica si el usuario está autenticado
   */
  isAuthenticated(): boolean {
    const token = this.getToken();
    return !!token && this.isAuthenticatedSubject.value;
  }
}
