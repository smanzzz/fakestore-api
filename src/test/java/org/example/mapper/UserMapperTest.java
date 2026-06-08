package org.example.mapper;

import org.example.dto.UserRequestCreateDTO;
import org.example.dto.UserResponseDTO;
import org.example.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    @Test
    void toDTO_ShouldMapUserToResponseDTO() {
        UserMapper userMapper = new UserMapper();

        User user = new User();
        user.setEmail("customer@example.com");
        user.setUsername("customer");

        UserResponseDTO result = userMapper.toDTO(user);

        assertEquals("customer@example.com", result.email());
        assertEquals("customer", result.username());
    }

    @Test
    void fromDTO_ShouldMapCreateRequestToUser() {
        UserMapper userMapper = new UserMapper();

        UserRequestCreateDTO request = new UserRequestCreateDTO(
                "customer",
                "customer@example.com",
                "Password1");

        User result = userMapper.fromDTO(request);

        assertEquals("customer", result.getUsername());
        assertEquals("customer@example.com", result.getEmail());
    }
}
