import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth-service';
import { CommonModule } from '@angular/common';

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

    constructor(private authService: AuthService){}

    onSubmit(){
        this.authService.register(this.registerFrom).subscribe({
            next: (res) => {
                alert(`El usuario ha sido registado: ${res.message}`);
                //Se ha registrado el usuario que he insertado pero no me entra aquí
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
