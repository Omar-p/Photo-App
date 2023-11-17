package com.example.photoapp.api.users;

import java.util.UUID;

public record AppUser(
    String firstName,
    String lastName,
    UUID userId,
    String email
) {
}
