package org.example.service;

import org.example.dto.UserRequestCreateDTO;
import org.example.dto.UserRequestLoginDTO;
import org.example.dto.UserResponseDTO;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    public void createUserTest() {
        // Arrange
        UserRequestCreateDTO testReqDTO = new UserRequestCreateDTO(
                "Habib",
                "habib@gmail.com",
                "Hejsan1");

        UserResponseDTO expectedResponse = new UserResponseDTO(
                1L,
                testReqDTO.username(),
                testReqDTO.email());

        User mappedUser = new User();
        mappedUser.setUsername(testReqDTO.username());
        mappedUser.setEmail(testReqDTO.email());


        when(userRepository.existsByUsername(testReqDTO.username())).thenReturn(false);

        when(userRepository.existsByEmail(testReqDTO.email())).thenReturn(false);

        when(userMapper.fromDTO(testReqDTO)).thenReturn(mappedUser);

        when(passwordEncoder.encode(testReqDTO.password())).thenReturn("krypterat_lösenord_123");

        when(userRepository.save(any(User.class))).thenReturn(mappedUser);

        when(userMapper.toDTO(any(User.class))).thenReturn(expectedResponse);


        //Act
        UserResponseDTO result = userService.createUser(testReqDTO);

        //Assert
        assertNotNull(result);
        assertEquals(expectedResponse.username(), result.username());
        assertEquals(expectedResponse.email(), result.email());
        assertEquals(expectedResponse.id(), result.id());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenUsernameAlreadyExists() {
        //Arrange, skapa de objekt som metoden använder, vi skickar in en requestdto

        UserRequestCreateDTO testReqDTO = new UserRequestCreateDTO(
                "Habib",
                "habib@gmail.com",
                "Hejsan1");

        when(userRepository.existsByUsername(testReqDTO.username())).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> {
                    userService.createUser(testReqDTO);
                });


        //Eftersom det första som sker i servicen när man vill skapa en user är att kolla ifall
        //Username existerar så tror jag inte man behöver skriva fler when eller anropa
        //mappers eller skapa andra objekt eftersom koden efter en throw inte körs.


    }
    @Test
    public void shouldThrowIllegalArgumentExceptionWhenEmailAlreadyExists() {
        //Arrange, skapa de objekt som metoden använder, vi skickar in en requestdto

        UserRequestCreateDTO testReqDTO = new UserRequestCreateDTO(
                "Habib",
                "habib@gmail.com",
                "Hejsan1");

        when(userRepository.existsByUsername(testReqDTO.username())).thenReturn(false);
        when(userRepository.existsByEmail(testReqDTO.email())).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> {
                    userService.createUser(testReqDTO);
                });
    }

    @Test
    public void shouldLoginUser(){

        // Arrange
        UserRequestLoginDTO reqDTO = new UserRequestLoginDTO(
                "Habib",
                "Hejsan1");

        String expectedJwtString = "en-lång-jwt-string";

        User mockUserLoggingIn = new User();

        mockUserLoggingIn.setUsername(reqDTO.username());
        mockUserLoggingIn.setPassword(reqDTO.password());

        Optional<User> userLoggingIn = Optional.of(mockUserLoggingIn);

        when(userRepository.findByUsername(reqDTO.username())).thenReturn(userLoggingIn);

        when(passwordEncoder.matches(reqDTO.password(),userLoggingIn.get().getPassword())).thenReturn(true);

        when(jwtService.generateToken(userLoggingIn.get().getUsername())).thenReturn(expectedJwtString);

        String result = userService.loginUser(reqDTO);

        assertEquals(result,  expectedJwtString);

    }

    @Test
    public void shouldThrowIllegalArgumentExceptionAndNotLoginUser(){

        // Arrange
        UserRequestLoginDTO reqDTO = new UserRequestLoginDTO(
                "Habib",
                "Hejsan1");

        User mockUserLoggingIn = new User();

        mockUserLoggingIn.setUsername(reqDTO.username());

        when(userRepository.findByUsername(reqDTO.username())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                ()-> {
                    userService.loginUser(reqDTO);
                });
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionAndNotLoginUserBecauseOfPasswordMisMatch(){

        // Arrange

        UserRequestLoginDTO reqDTO = new UserRequestLoginDTO(
                "Habib",
                "Hejsan1");

        User mockUserLoggingIn= new User();

        mockUserLoggingIn.setUsername(reqDTO.username());
        mockUserLoggingIn.setPassword(reqDTO.password());

        Optional<User> userLoggingIn = Optional.of(mockUserLoggingIn);

        when(userRepository.findByUsername(reqDTO.username())).thenReturn(userLoggingIn);

        when(passwordEncoder.matches(reqDTO.password(),userLoggingIn.get().getPassword())).thenReturn(false);

         assertThrows(IllegalArgumentException.class,
               ()-> {
                   userService.loginUser(reqDTO);
               });


    }

    @Test
    public void shouldReturnAllUsersInList(){

        //Arrange

        User user = new User();

        List<User> usersList = List.of(user);
        UserResponseDTO expectedRespDTO = new UserResponseDTO(1L,
                "Habib",
                "habib@gmail.com");
        //Act
        when(userRepository.findAll()).thenReturn(usersList);
        when(userMapper.toDTO(any(User.class))).thenReturn(expectedRespDTO);

        List<UserResponseDTO> usersListResult = userService.findAllUsers();

        //Assert
        assertFalse(usersListResult.isEmpty());
        assertEquals(1, usersListResult.size());
        assertEquals("Habib", usersListResult.getFirst().username());

    }




}

