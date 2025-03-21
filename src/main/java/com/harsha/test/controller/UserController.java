package com.harsha.test.controller;

import com.harsha.test.dto.UserRequestDto;
import com.harsha.test.dto.UserResponseDto;
import com.harsha.test.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class UserController {
  private final UserService userService;

  @GetMapping("users")
  public ResponseEntity<List<UserResponseDto>> getAllUsers() {
    List<UserResponseDto> users = userService.getAllUsers();
    return ResponseEntity.status(HttpStatus.OK).body(users);
  }

  @GetMapping("users/{id}")
  public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
    UserResponseDto userResponseDto = userService.getUserById(id);
    return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
  }

  @PostMapping("users")
  public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) {
    UserResponseDto savedUser = userService.createUser(userRequestDto);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedUser.getId())
            .toUri();
    return ResponseEntity.created(uri).body(savedUser);
  }

  @DeleteMapping("users/{id}")
  public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
    userService.deleteUserById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
