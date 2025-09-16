import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '', //Funciona de esta forma. 
        loadComponent: () => import('./pages/dashboard/dashboard').then(m => m.Dashboard),
        children: [
            {
                path: '', // redirección por defecto cargue el Login al acceder al dashboard directamente
                redirectTo: 'login',
                pathMatch: 'full'
            },
            {
                path: 'login',
                loadComponent: () => import('./auth/components/login/login').then(m => m.Login)
            },
            {
                path: 'register',
                loadComponent: () => import('./auth/components/register/register').then(m => m.Register)
            },
            {
                path: 'email-verificator',
                loadComponent: () => import('./auth/components/email-verificator/email-verificator').then(m => m.EmailVerificator)
            }
        ]
    }
];
