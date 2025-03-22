package com.harsha.test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harsha.test.dto.UserRequestDto;
import com.harsha.test.dto.UserResponseDto;
import com.harsha.test.entity.User;
import com.harsha.test.exceptions.IllegalValueException;
import com.harsha.test.exceptions.UserNotFoundException;
import com.harsha.test.repository.UserRepository;
import com.harsha.test.utils.Constants;
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
        .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND + id));
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

  public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
    if (id == null) throw new IllegalValueException("id cannot be null");
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND + id));
    if (userRequestDto.getUserName() != null) user.setUserName(userRequestDto.getUserName());
    if (userRequestDto.getEmail() != null) user.setEmail(userRequestDto.getEmail());
    User updatedUser = userRepository.save(user);
    logInfoWithId(updatedUser.getId(), "updated");
    return mapToUserResponseDto(updatedUser);
  }

  public void deleteUserById(Long id) {
    if (!userRepository.existsById(id))
      throw new UserNotFoundException(Constants.USER_NOT_FOUND + id);
    userRepository.deleteById(id);
    logInfoWithId(id, "deleted");
  }

  public void logInfoWithId(Long id, String operation) {
    log.info("user with id {} {} successfully", id, operation);
  }
}
