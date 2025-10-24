package com.serp.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);

    // Alias: erlaubt weiterhin findByEmail(...) im Controller,
    // obwohl die Spalte "username" heißt.
    @Query("select u from UserEntity u where u.username = :email")
    Optional<UserEntity> findByEmail(@Param("email") String email);
}
