package com.example.E_commerce.Exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException accessDeniedException ) {
        String message = "you do not have permission to access this resource";
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);

    }
}
