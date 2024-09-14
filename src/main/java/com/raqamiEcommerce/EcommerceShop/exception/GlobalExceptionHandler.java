package com.raqamiEcommerce.EcommerceShop.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {
    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<String>handleException(AccessDeniedException ex, HttpServletRequest request) {
            String message="You don't have permission to access this resource";
            return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);

    }
}
