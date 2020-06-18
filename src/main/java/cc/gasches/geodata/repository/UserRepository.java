package cc.gasches.geodata.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cc.gasches.geodata.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

    boolean existsByName(String name);
}
