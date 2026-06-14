package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.example.StrongPassword;

public record UserRequestCreateDTO(
        @NotBlank(message = "Användarnamn får inte vara tomt.")
        @Size(min = 3, max = 50)
        String username,

        @NotBlank(message = "E-post får inte vara tom.")
        @Pattern(
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "E-postadressen måste ha ett giltigt format.")
        String email,

        @StrongPassword
        @NotBlank(message = "Lösenordet får inte vara tomt.")
        @Size(min = 1, max = 40, message = "Lösenordet måste vara mellan 1 och 40 tecken.")
        String password) {
}
