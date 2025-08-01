import { Component } from '@angular/core';
import { FormGroup, FormControl, ReactiveFormsModule, EmailValidator } from '@angular/forms';
import { LoginService } from '../../services/login-service';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [
        ReactiveFormsModule
    ],
    templateUrl: './login.html',
    styleUrl: './login.css'
})
export class Login {
    profileForm = new FormGroup({
        email: new FormControl(''),
        password: new FormControl('')
    })

    constructor(private loginService: LoginService) { }

    onSubmit() { //Simplemente enseño los mensajes que devuelve el back-end
        const email = this.profileForm.value.email;
        const pass = this.profileForm.value.password;
        alert(`${email}, ${pass}`);
        this.loginService.login(this.profileForm).subscribe({
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
