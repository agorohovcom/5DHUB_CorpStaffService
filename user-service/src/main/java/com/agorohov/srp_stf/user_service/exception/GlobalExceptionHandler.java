package com.agorohov.srp_stf.user_service.exception;

import com.agorohov.srp_stf.user_service.dto.ErrorMessage;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> employeeNotFoundException(UserNotFoundException e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(e.getMessage());
        errorMessage.setTime(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> pageNotFoundException(PageNotFoundException e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(e.getMessage());
        errorMessage.setTime(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> mapperException(MapperException e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage("Internal server error, please try again later");
        errorMessage.setTime(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> feignException(FeignException e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage("Server is not available now, try again later");
        errorMessage.setTime(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
}
