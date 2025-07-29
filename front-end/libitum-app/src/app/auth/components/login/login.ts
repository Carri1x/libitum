import { Component } from '@angular/core';
import { FormGroup,FormControl, ReactiveFormsModule, EmailValidator } from '@angular/forms';

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
}
