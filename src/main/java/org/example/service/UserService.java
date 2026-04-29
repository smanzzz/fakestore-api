package org.example.service;

import org.example.dto.UserRequestCreateDTO;
import org.example.dto.UserRequestLoginDTO;
import org.example.dto.UserResponseDTO;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtService jwtService ){

        this.userRepository=userRepository;
        this.userMapper=userMapper;
        this.passwordEncoder=passwordEncoder;
        this.jwtService=jwtService;

    }

    public List<UserResponseDTO> findAllUsers() {
        List<User> usersList = userRepository.findAll();

       return usersList
               .stream()
               .map(userMapper::toDTO)
                         .toList();

    }

    public UserResponseDTO createUser(UserRequestCreateDTO reqDTO) {

        if (userRepository.existsByUsername(reqDTO.username())) {
            throw new IllegalArgumentException("Det användarnamnet är upptaget.");

        }
        if (userRepository.
                existsByEmail(reqDTO.email())) {

            throw new IllegalArgumentException("Det Emailet är redan i bruk.");
        }
        User newUser = userMapper.fromDTO(reqDTO);
        newUser.setPassword(passwordEncoder.encode(reqDTO.password()));
        User savedUser = userRepository.save(newUser);

        return  userMapper.toDTO(savedUser);

    }

    public String loginUser(UserRequestLoginDTO requestLoginDTO) {

        Optional<User> userLoggingIn = userRepository.findByUsername(requestLoginDTO.username());

        if (userLoggingIn.isEmpty()){

            throw new IllegalArgumentException("Fel användarnamn eller lösenord.");
        }

        if (!passwordEncoder.matches(requestLoginDTO.password(), userLoggingIn.get().getPassword())){
            throw new IllegalArgumentException("Fel användarnamn eller lösenord.");
        }

       return jwtService.generateToken(userLoggingIn.get().getUsername());


    }
 //steg 1 ta emot requesten. Steg 2 kollar ifall de matchar username finns. Steg 3 om den finns kolla ifall lösen matchar.
    //Steg 4 ifall den inte matchar kasta fel och informera om.
    //stef 5 returnera en UserresponseDTO
}
