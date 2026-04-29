package org.example.controller;

import org.example.dto.UserRequestCreateDTO;
import org.example.dto.UserRequestLoginDTO;
import org.example.dto.UserResponseDTO;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService){
     this.userService=userService;
    }


    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAllUsers() {

        List<UserResponseDTO> responseDTOList = userService.findAllUsers();

        return ResponseEntity.ok(responseDTOList);

    }

    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestCreateDTO reqDTO){
        UserResponseDTO responseDTO = userService.createUser(reqDTO);
        return ResponseEntity.ok(responseDTO);
    }


    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserRequestLoginDTO reqDTO){
      String jwtString = userService.loginUser(reqDTO);

        return ResponseEntity.ok(jwtString);

    }




}
