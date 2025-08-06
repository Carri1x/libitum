import { Component, OnInit, inject, OnDestroy } from '@angular/core';
import { Router, RouterOutlet, NavigationEnd } from '@angular/router';
import { filter, Subscription } from 'rxjs';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [
        RouterOutlet
    ],
    templateUrl: './dashboard.html',
    styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit, OnDestroy {
    private router = inject(Router);
    private routeSub!: Subscription;

    ngOnInit() {
        
        this.routeSub = this.router.events
            .pipe(filter(event => event instanceof NavigationEnd))
            .subscribe((event: any) => {
                if (event.url.includes('/login')) {
                    document.body.className = 'login-theme';
                } else if (event.url.includes('/register')) {
                    document.body.className = 'register-theme';
                } else {
                    document.body.className = ''; // o algún tema por defecto
                }
            });
    }

    ngOnDestroy() {
        this.routeSub?.unsubscribe();
    }
}
