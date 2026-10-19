package es.uclm.Nester.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uclm.Nester.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}