package com.libitum.app.repositories;

import com.libitum.app.model.Role;
import com.libitum.app.model.enums.RoleList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleList nameRol);
}
