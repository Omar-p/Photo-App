package com.example.photoapp.api.users.login;

import jakarta.validation.constraints.Email;

record UserLoginRequest(
    @Email
    String email,
    String password
) {
}
