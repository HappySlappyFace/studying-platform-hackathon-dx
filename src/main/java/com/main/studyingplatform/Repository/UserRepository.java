// src/main/java/com/main/itmanagement/Repository/UserRepository.java

package com.main.studyingplatform.Repository;

import com.main.studyingplatform.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}
