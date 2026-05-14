package org.example.mapper;


import org.example.dto.UserRequestCreateDTO;
import org.example.dto.UserResponseDTO;
import org.example.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    @Test
    void userMapperTestToDTO(){

        UserMapper userMapper = new UserMapper();

        User newTestUser = new User();
        String email = "kalle@gmail.com";
        String username = "Kalle";

        newTestUser.setEmail(email);
        newTestUser.setUsername(username);

        UserResponseDTO toDTO = userMapper.toDTO(newTestUser);

        assertEquals(toDTO.email(), email);
        assertEquals(toDTO.username(), username);

    }

    @Test
    void userMapperTestFromDTO(){
        UserMapper userMapper = new UserMapper();

        String username = "Fredrik";
        String email = "fredrik@gmail.com";
        String password = "Hejsan1";

        UserRequestCreateDTO userReqDTO = new UserRequestCreateDTO(username, email, password);


        User user = userMapper.fromDTO(userReqDTO);

        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());

    }




}
