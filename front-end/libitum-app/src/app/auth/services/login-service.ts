import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
    private readonly apiUrl = 'http://localhost:8080/auth';

    constructor(private httpClient: HttpClient) {}

    login(form : FormGroup): Observable<string>{
        const email = form.value.email;
        const password = form.value.password;
        const body = {email, password};
        return this.httpClient.post<string>(`${this.apiUrl}/login`,body);
    }
}
