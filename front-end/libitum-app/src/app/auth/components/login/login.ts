import { Component} from '@angular/core';
import { FormGroup, FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth-service';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule
    ],
    templateUrl: './login.html',
    styleUrl: './login.css'
})
export class Login {
    profileForm = new FormGroup({
        email: new FormControl<string>('', [Validators.required, Validators.email]),
        password: new FormControl<string>('', Validators.required)
    })

    constructor(private authService: AuthService) { }

    get email() {
        return this.profileForm.get('email')!;//El operador "!" dice esto se que no es null confía en mi.
    }

    get password() {
        return this.profileForm.get('password')!;
    }


    onSubmit() { //Simplemente enseño los mensajes que devuelve el back-end
        const email = this.profileForm.value.email;
        const pass = this.profileForm.value.password;
        alert(`${email}, ${pass}`);
        this.authService.login(this.profileForm).subscribe({
            next: (res: any) => {
                console.log('Login existoso del backend:', res);
                localStorage.setItem('jwt', res.token); //Aquí meto como los servlets en "la sesion" el token
                alert('Login exitoso');
            },
            error: (err: any) => {
                alert(`entrando en error: ${err.token}`);
                alert(err.value)
            }
        })
    }
}
