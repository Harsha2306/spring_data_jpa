package com.harsha.test.service;

import com.harsha.test.dto.PaginatedResponseDto;
import com.harsha.test.dto.UserRequestDto;
import com.harsha.test.dto.UserResponseDto;
import com.harsha.test.entity.User;
import com.harsha.test.exceptions.IllegalValueException;
import com.harsha.test.exceptions.PageLimitExceededException;
import com.harsha.test.exceptions.UserNotFoundException;
import com.harsha.test.repository.UserRepository;
import com.harsha.test.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_WITH_ID + id));
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
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_WITH_ID + id));
    if (userRequestDto.getUserName() != null) user.setUserName(userRequestDto.getUserName());
    if (userRequestDto.getEmail() != null) user.setEmail(userRequestDto.getEmail());
    User updatedUser = userRepository.save(user);
    logInfoWithId(updatedUser.getId(), "updated");
    return mapToUserResponseDto(updatedUser);
  }

  public void deleteUserById(Long id) {
    if (!userRepository.existsById(id))
      throw new UserNotFoundException(Constants.USER_NOT_FOUND_WITH_ID + id);
    userRepository.deleteById(id);
    logInfoWithId(id, "deleted");
  }

  public void logInfoWithId(Long id, String operation) {
    log.info("user with id {} {} successfully", id, operation);
  }

  public UserResponseDto getUserByUserName(String userName) {
    if (userName == null || userName.isBlank())
      throw new IllegalValueException("userName cannot be null or empty");
    return userRepository
        .findByUserName(userName)
        .map(this::mapToUserResponseDto)
        .orElseThrow(
            () -> new UserNotFoundException(Constants.USER_NOT_FOUND_WITH_USERNAME + userName));
  }

  public List<UserResponseDto> findLikeUserName(String userName) {
    if (userName == null || userName.isBlank())
      throw new IllegalValueException("userName cannot be null or empty");
    return userRepository.findLikeUserName(userName).stream()
        .map(this::mapToUserResponseDto)
        .toList();
  }

  public PaginatedResponseDto<UserResponseDto> getUsersByCriteria(
      int page, int pageSize, String sortBy, String sortOrder) {
    Set<String> allowedSortFields = Set.of("id", "userName", "email");
    if (!allowedSortFields.contains(sortBy))
      throw new IllegalValueException("sortBy cannot be: " + sortBy);
    Sort sort =
        Sort.by(
            sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
    Pageable pageable = PageRequest.of(page, pageSize, sort);
    Page<User> userPage = userRepository.findAll(pageable);
    if (userPage.getTotalPages() < page)
      throw new PageLimitExceededException(
          "Page limit exceeded, max Pages: " + userPage.getTotalPages());
    List<UserResponseDto> users =
        userPage.getContent().stream().map(this::mapToUserResponseDto).toList();
    log.info(
        "criteria - page {}, pageSize {}, sortBy {}, sortOrder {} ",
        page,
        pageSize,
        sortBy,
        sortOrder);
    return PaginatedResponseDto.<UserResponseDto>builder()
        .data(users)
        .totalUsers(userPage.getTotalElements())
        .totalPages(userPage.getTotalPages())
        .currentPage(page)
        .pageSize(pageSize)
        .links(buildPaginationLinks(page, pageSize, sortBy, sortOrder, userPage.getTotalPages()))
        .build();
  }

  private Map<String, String> buildPaginationLinks(
      int page, int pageSize, String sortBy, String sortOrder, int totalPages) {
    Map<String, String> links = new HashMap<>();
    String baseUrl = "api/users/criteria";
    if (page > 0) {
      links.put("prev", buildPageLink(baseUrl, page - 1, pageSize, sortBy, sortOrder));
    }
    if (page < totalPages - 1) {
      links.put("next", buildPageLink(baseUrl, page + 1, pageSize, sortBy, sortOrder));
    }
    return links;
  }

  private String buildPageLink(
      String baseUrl, int page, int pageSize, String sortBy, String sortOrder) {
    return baseUrl
        + "?"
        + "page="
        + page
        + "&pageSize="
        + pageSize
        + "&sortBy="
        + sortBy
        + "&sortOrder="
        + sortOrder;
  }
}
