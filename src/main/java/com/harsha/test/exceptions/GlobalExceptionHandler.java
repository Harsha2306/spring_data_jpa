package com.harsha.test.exceptions;

import com.harsha.test.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException ex) {
    ErrorResponseDto error = new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(IllegalValueException.class)
  public ResponseEntity<ErrorResponseDto> handleIllegalValueException(IllegalValueException ex) {
    ErrorResponseDto error = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(PageLimitExceededException.class)
  public ResponseEntity<ErrorResponseDto> handlePageLimitExceededException(
      PageLimitExceededException ex) {
    ErrorResponseDto error = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(GenericException.class)
  public ResponseEntity<ErrorResponseDto> handleGenericException(GenericException ex) {
    ErrorResponseDto error = new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
