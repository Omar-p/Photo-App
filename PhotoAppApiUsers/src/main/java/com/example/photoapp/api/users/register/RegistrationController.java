package com.example.photoapp.api.users.register;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
class RegistrationController {
  private final UserRegistrationService userRegistrationService;


  public RegistrationController(UserRegistrationService userRegistrationService) {
    this.userRegistrationService = userRegistrationService;
  }

  @PostMapping
  ResponseEntity<Void> registerUser(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest) {
    var userUri = userRegistrationService.registerNewUser(userRegistrationRequest);
    return ResponseEntity.created(userUri).build();
  }

}
