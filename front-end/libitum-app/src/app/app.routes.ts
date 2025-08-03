import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '', //Funciona de esta forma. 
        loadComponent: () => import('./dashboard/dashboard').then(m => m.Dashboard)
    },
    {
        path: 'login',
        loadComponent: () => import('./auth/components/login/login').then(m => m.Login)
    },
    {
        path: 'register',
        loadComponent: () => import('./auth/components/register/register').then(m => m.Register)
    }
];
