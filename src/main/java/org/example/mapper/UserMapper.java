package org.example.mapper;

import org.example.dto.UserRequestCreateDTO;
import org.example.dto.UserResponseDTO;
import org.example.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toDTO(User user) {

        UserResponseDTO respDTO = new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail());


        return respDTO;
    }


    public User fromDTO (UserRequestCreateDTO reqDTO){
        User user = new User();

                user.setUsername(reqDTO.username());
                user.setEmail(reqDTO.email());

        return user;
    }

    //Deklarera metoden med det vi ska returnera, en UserresponseDTO sen ska vi sätta värdena från user och sen returnera respdto

}
