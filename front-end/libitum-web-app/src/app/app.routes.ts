import { Routes } from '@angular/router';
import { AuthCallbackComponent } from './auth/google/components/auth-callback/auth-callback';

export const routes: Routes = [
    {
        path: 'auth/success',
        component: AuthCallbackComponent
    },
    {
        path: 'auth/error',
        component: AuthCallbackComponent
    }
];
