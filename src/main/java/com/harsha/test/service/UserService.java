package com.harsha.test.service;

import com.harsha.test.dto.UserRequestDto;
import com.harsha.test.dto.UserResponseDto;
import com.harsha.test.entity.User;
import com.harsha.test.exceptions.UserNotFoundException;
import com.harsha.test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
  private final UserRepository userRepository;

  public List<UserResponseDto> getAllUsers() {
    return userRepository.findAll().stream().map(this::mapToUserResponseDto).toList();
  }

  public UserResponseDto getUserById(Long id) {
    return userRepository
        .findById(id)
        .map(this::mapToUserResponseDto)
        .orElseThrow(() -> new UserNotFoundException("user not found with id: " + id));
  }

  public UserResponseDto mapToUserResponseDto(User user) {
    return UserResponseDto.builder()
        .userName(user.getUserName())
        .id(user.getId())
        .email(user.getEmail())
        .build();
  }

  public UserResponseDto createUser(UserRequestDto userRequestDto) {
    User user =
        User.builder()
            .email(userRequestDto.getEmail())
            .userName(userRequestDto.getUserName())
            .build();
    User savedUser = userRepository.save(user);
    logInfoWithId(savedUser.getId(), "saved");
    return mapToUserResponseDto(savedUser);
  }

  public void deleteUserById(Long id) {
    if (!userRepository.existsById(id))
      throw new UserNotFoundException("user not found with id: " + id);
    userRepository.deleteById(id);
    logInfoWithId(id, "deleted");
  }

  public void logInfoWithId(Long id, String operation) {
    log.info("user with id {} {} successfully", id, operation);
  }
}
