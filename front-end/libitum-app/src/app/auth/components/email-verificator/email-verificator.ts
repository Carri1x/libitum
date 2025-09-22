import { Component } from '@angular/core';
import { FormControl, ReactiveFormsModule, FormGroup, Validators, ɵInternalFormsSharedModule } from '@angular/forms';
import { AuthService } from '../../services/auth-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-email-verificator',
  standalone: true,
  imports: [
    ɵInternalFormsSharedModule,
    ReactiveFormsModule
    ],
  templateUrl: './email-verificator.html',
  styleUrl: './email-verificator.css'
})
export class EmailVerificator {
  emailVerificatorForm = new FormGroup({
    numberVerificator: new FormControl('', Validators.required)
  });

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  checkEmailTokenVerification() {
    const token = this.emailVerificatorForm.value.numberVerificator!;

    // Validar que sea un número
    if (!/^\d+$/.test(token)) {
      console.log("El código debe ser un número que te hemos enviado al correo");
      return;
    }

    // Si es un número, ya puedes llamar al backend
    this.authService.emailTokenVerification(token).subscribe({
      next: (res) => {
        console.log("✅ Has conseguido insertar bien el código que te hemos pasado en el correo");
        this.router.navigate(['home']);
      },
      error: (err) => {
        console.log("❌ Ha dado algún error: " + (err.error?.message || err.message));
      }
    });
  }
}

