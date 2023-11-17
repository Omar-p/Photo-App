package com.example.photoapp.api.users;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email", name = "users_email_key"),
        @UniqueConstraint(columnNames = "userId", name = "users_user_id_key")
    }
)
public class User {
  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "users_id_seq"
  )
  @SequenceGenerator(
      name = "users_id_seq",
      sequenceName = "users_id_seq",
      allocationSize = 1
  )
  private Long id;

  @Column(nullable = false, length = 50, name = "first_name")
  private String firstName;
  @Column(nullable = false, length = 50, name = "last_name")
  private String lastName;
  @Column(nullable = false, length = 120)
  private String email;
  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private UUID userId;

  public User() {
  }

  public User(User user) {
    this.id = user.id;
    this.firstName = user.firstName;
    this.lastName = user.lastName;
    this.email = user.email;
    this.password = user.password;
    this.userId = user.userId;
  }



  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }
}
