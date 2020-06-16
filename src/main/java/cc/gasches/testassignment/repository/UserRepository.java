package cc.gasches.testassignment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cc.gasches.testassignment.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

    boolean existsByName(String name);
}
