package com.harsha.test.controller;

import com.harsha.test.dto.PaginatedResponseDto;
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
  public ResponseEntity<List<UserResponseDto>> getUsers() {
    List<UserResponseDto> users = userService.getAllUsers();
    return ResponseEntity.status(HttpStatus.OK).body(users);
  }

  @GetMapping("criteria")
  public ResponseEntity<PaginatedResponseDto<UserResponseDto>> getUsersByCriteria(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "asc") String sortOrder) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.getUsersByCriteria(page, pageSize, sortBy, sortOrder));
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
