package com.agorohov.cs_service.staff_service.exception;

import com.agorohov.cs_service.staff_service.dto.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> employeeNotFoundException(EmployeeNotFoundException e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(e.getMessage());
        errorMessage.setTime(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
}
