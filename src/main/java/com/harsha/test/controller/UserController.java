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
@RequestMapping("api/users")
public class UserController {
  private final UserService userService;

  @GetMapping
  public ResponseEntity<List<UserResponseDto>> getAllUsers() {
    List<UserResponseDto> users = userService.getAllUsers();
    return ResponseEntity.status(HttpStatus.OK).body(users);
  }

  @GetMapping("{id}")
  public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
    UserResponseDto userResponseDto = userService.getUserById(id);
    return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
  }

  @GetMapping("byUserName")
  public ResponseEntity<UserResponseDto> getUserByUserName(@RequestParam String userName) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByUserName(userName));
  }

  @GetMapping("likeUserName")
  public ResponseEntity<List<UserResponseDto>> getUserByLikeUserName(
      @RequestParam String userName) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.findLikeUserName(userName));
  }

  @PostMapping
  public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) {
    UserResponseDto savedUser = userService.createUser(userRequestDto);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedUser.getId())
            .toUri();
    return ResponseEntity.created(uri).body(savedUser);
  }

  @PutMapping("{id}")
  public ResponseEntity<UserResponseDto> updateUser(
      @PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
    UserResponseDto updatedUser = userService.updateUser(id, userRequestDto);
    return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
    userService.deleteUserById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
