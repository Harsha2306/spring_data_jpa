package com.harsha.test.service;

import com.harsha.test.dto.UserRequestDto;
import com.harsha.test.dto.UserResponseDto;
import com.harsha.test.entity.User;
import com.harsha.test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
  private final UserRepository userRepository;

  public List<UserResponseDto> getAllUsers() {
    return userRepository.findAll().stream().map(this::mapToUserResponseDto).toList();
  }

  // TODO handle no id found using exception
  public UserResponseDto getUserById(Long id) {
    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isEmpty()) {}
    return mapToUserResponseDto(optionalUser.get());
  }

  public UserResponseDto mapToUserResponseDto(User user) {
    return UserResponseDto.builder()
        .userName(user.getUserName())
        .id(user.getId())
        .email(user.getEmail())
        .build();
  }

  public void createUser(UserRequestDto userRequestDto) {
    User user =
        User.builder()
            .email(userRequestDto.getEmail())
            .userName(userRequestDto.getUserName())
            .build();
    User savedUser = userRepository.save(user);
    log.info("user with id {} saved", savedUser.getId());
  }

  // TODO handle no id found using exception and put, patch
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}
