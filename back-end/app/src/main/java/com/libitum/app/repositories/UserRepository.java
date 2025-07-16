package com.libitum.app.repositories;

import com.libitum.app.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByName(String username);
    Optional<User> findByEmail(String email);
    boolean existsByName(String name);
}
