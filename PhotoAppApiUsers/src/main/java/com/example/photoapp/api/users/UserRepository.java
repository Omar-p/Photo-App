package com.example.photoapp.api.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String username);

  @Query("SELECT new com.example.photoapp.api.users.AppUser(u.firstName, u.lastName, u.userId, u.email) FROM User u WHERE u.email = ?1")
  Optional<AppUser> findAppUserByEmail(String email);
}
