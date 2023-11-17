package com.example.photoapp.api.users.register;

import com.example.photoapp.api.users.User;
import com.example.photoapp.api.users.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Service
class UserRegistrationService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  UserRegistrationService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.passwordEncoder = passwordEncoder;
  }

  URI registerNewUser(UserRegistrationRequest userRegistrationRequest) {
    var user = userMapper.userRegistrationRequestToUser(userRegistrationRequest);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setUserId(UUID.randomUUID());
    var savedUser = userRepository.save(user);
    return buildUserUrl(savedUser);
  }

  private URI buildUserUrl(User user) {
    return UriComponentsBuilder.fromPath("/users/{id}")
        .buildAndExpand(user.getUserId())
        .toUri();
  }
}
