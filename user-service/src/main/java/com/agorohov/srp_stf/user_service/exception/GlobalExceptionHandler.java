package com.agorohov.srp_stf.user_service.exception;

import com.agorohov.srp_stf.user_service.dto.ErrorMessage;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> employeeNotFoundException(UserNotFoundException e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(e.getMessage());
        errorMessage.setTime(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorMessage> feignException(FeignException e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage("Server is not available now, try again later");
        errorMessage.setTime(LocalDateTime.now());

        log.error("FeignException occurred: {}", e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError firstError = e.getBindingResult().getFieldErrors().get(0);
        String message = String.format("Validation failed in %s: %s",
                firstError.getField(), firstError.getDefaultMessage());

        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(message);
        errorMessage.setTime(LocalDateTime.now());

        log.error("MethodArgumentNotValidException occurred: {}", e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}
