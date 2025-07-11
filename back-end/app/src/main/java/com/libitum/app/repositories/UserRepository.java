package com.libitum.app.repositories;

import com.libitum.app.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByName(String name);
    boolean existByName(String name);
}
