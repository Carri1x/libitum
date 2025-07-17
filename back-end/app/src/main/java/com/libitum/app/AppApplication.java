package com.libitum.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación.
 * Inicia la aplicación Spring Boot.
 * 
 * @author Álvaro Carrión
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class AppApplication {
	/**
	 * Método principal para iniciar la aplicación.
	 * 
	 * @param args Argumentos de línea de comandos.
	 * @since 1.0
	 */
	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

}
