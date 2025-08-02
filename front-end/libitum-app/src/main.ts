import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';

bootstrapApplication(App,appConfig)
  .catch((err) => console.error(err));


/*Esto es lo que hubiera hecho en esta misma clase 
bootstrapApplication(App,{
  providers: [
    provideHttpClient()
  ]
})
  
Pero al tener en app.config.ts esto ==>>

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient()
  ]
};

 ==>> Puedo directamente insertar ese provider y no quitar la clase de app.config.ts
Quedando todo directamente más limpio y ordenado.
*/