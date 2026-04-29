package org.example.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequestLoginDTO(

        @NotBlank(message = "Får inte vara tomt.")
        String username,
        @NotBlank(message = "Får inte vara tomt.")
        String password) {
}
