package com.harsha.test.controller;

import com.harsha.test.dto.UserResponseDto;
import com.harsha.test.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class UserController {
  private final UserService userService;

  @GetMapping("users")
  public ResponseEntity<?> getAllUsers() {

  }
}
