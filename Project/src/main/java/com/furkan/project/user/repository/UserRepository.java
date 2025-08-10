package com.furkan.project.user.repository;

import com.furkan.project.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    // Lookup
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    // Eager roles when needed
    @EntityGraph(attributePaths = "roles")
    Optional<User> findWithRolesByUsername(String username);

    // Uniqueness checks
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}