package com.libitum.app.repositories;

import com.libitum.app.model.Role;
import com.libitum.app.model.enums.RoleList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para gestionar los roles de usuario en la base de datos.
 * Proporciona métodos para buscar roles por su nombre.
 * 
 * @author Álvaro Carrión
 * @version 1.0
 * @since 1.0

 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleList nameRol);
}
