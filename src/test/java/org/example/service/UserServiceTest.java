package org.example.service;

import org.example.dto.UserRequestCreateDTO;
import org.example.dto.UserRequestLoginDTO;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

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

    private User user;
    private UserRequestCreateDTO createDTO;
    private UserRequestLoginDTO loginDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("customer");
        user.setPassword("encodedPassword");
        user.setEmail("customer@example.com");

        createDTO = new UserRequestCreateDTO("customer", "customer@example.com", "Password123");
        loginDTO = new UserRequestLoginDTO("customer", "Password123");
    }

    @Test
    void createUser_Success() {
        when(userRepository.existsByUsername(createDTO.username())).thenReturn(false);
        when(userRepository.existsByEmail(createDTO.email())).thenReturn(false);
        when(passwordEncoder.encode(createDTO.password())).thenReturn("encodedPassword");
        when(userMapper.fromDTO(any())).thenReturn(user);

        assertDoesNotThrow(() -> userService.createUser(createDTO));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_ThrowsExceptionWhenUsernameExists() {
        when(userRepository.existsByUsername(createDTO.username())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(createDTO));

        assertEquals("Det användarnamnet är upptaget.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_ThrowsExceptionWhenEmailExists() {
        when(userRepository.existsByUsername(createDTO.username())).thenReturn(false);
        when(userRepository.existsByEmail(createDTO.email())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(createDTO));

        assertEquals("Den e-postadressen är redan i bruk.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginUser_Success_ReturnsToken() {
        when(userRepository.findByUsername(loginDTO.username())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDTO.password(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user.getUsername())).thenReturn("jwt-token");

        String token = userService.loginUser(loginDTO);

        assertEquals("jwt-token", token);
    }

    @Test
    void loginUser_ThrowsExceptionWhenPasswordIsIncorrect() {
        when(userRepository.findByUsername(loginDTO.username())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDTO.password(), user.getPassword())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.loginUser(loginDTO));

        assertEquals("Fel användarnamn eller lösenord.", exception.getMessage());
    }

    @Test
    void loginUser_ThrowsExceptionWhenUserNotFound() {
        when(userRepository.findByUsername(loginDTO.username())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.loginUser(loginDTO));

        assertEquals("Fel användarnamn eller lösenord.", exception.getMessage());
    }
}
