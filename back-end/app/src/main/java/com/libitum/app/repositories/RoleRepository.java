package com.libitum.app.repositories;

import com.libitum.app.model.Role;
import com.libitum.app.model.enums.RoleList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para gestionar los roles de usuario en la base de datos.
 * Proporciona métodos para buscar roles por su nombre.
 * @apiNote Importante saber que los roles dentro de la base de datos se guardan como enteros (ordinales) y no como cadenas de texto.
 * 
 * @author Álvaro Carrión
 * @version 1.0
 * @since 1.0
 * @apiNote Este repositorio es utilizado para acceder a los roles de usuario en la base de datos.
 * @category Seguridad
 * @subcategory Repositorios
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleList nameRol);
}
