import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private readonly apiUrl = 'http://localhost:8080/auth';

    constructor(private httpClient: HttpClient) { }

    login(form: FormGroup): Observable<any> {
        return this.httpClient.post<any>(`${this.apiUrl}/login`, form.value);
    }

    register(from: FormGroup): Observable<any> {
        return this.httpClient.post<any>(`${this.apiUrl}/register`,from.value);
    }
}
