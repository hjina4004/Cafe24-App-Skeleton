package com.lmfriends.cafe24app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lmfriends.cafe24app.model.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String name);
}
