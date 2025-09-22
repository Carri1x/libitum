import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth-service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule
  ],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {
    registerFrom = new FormGroup({
        name: new FormControl('', Validators.required),
        surname: new FormControl(''),
        email: new FormControl<string>('', [Validators.required, Validators.email]),
        password: new FormControl('', Validators.required),
        nickname: new FormControl(''),
        phoneNumber: new FormControl('')
    });

    get name() {
        return this.registerFrom.get('name')!;//El operador "!" dice esto se que no es null confía en mi.
    }

    get email() {
        return this.registerFrom.get('email')!;//El operador "!" dice esto se que no es null confía en mi.
    }

    get password() {
        return this.registerFrom.get('password')!;//El operador "!" dice esto se que no es null confía en mi.
    }

    constructor(
        private authService: AuthService,
        private router: Router
    ){}

    onSubmit(){
        this.authService.register(this.registerFrom).subscribe({
            next: (res) => {
                console.log("El usuario ha pasado por register.ts onSubmit() -> res: "+ res.message)
                //Aquí vamos a llevarlo al componente de verificación.
                this.router.navigate(['email-verificator']);
            },
            error: (err) => {
                if(err.status === 400){
                    alert(`${err.error.message}`);
                }
                else{
                    alert(`${err.error.message}`);
                }
            }
        })
    }
}
