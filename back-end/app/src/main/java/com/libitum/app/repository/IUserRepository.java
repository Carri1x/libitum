package com.libitum.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libitum.app.model.UserDTO;

public interface IUserRepository extends JpaRepository<UserDTO, Long>{
    Optional<UserDTO> findByEmail(String email);
}
