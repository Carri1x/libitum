package com.libitum.app.repositories;

import com.libitum.app.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para gestionar los usuarios en la base de datos.
 * Proporciona métodos para buscar usuarios por nombre y email, y verificar la existencia de un usuario por nombre.
 * 
 * @author Álvaro Carrión
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByName(String username);
    Optional<User> findByEmail(String email);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}
