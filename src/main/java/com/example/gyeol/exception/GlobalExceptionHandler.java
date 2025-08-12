// src/main/java/com/example/gyeol/exception/GlobalExceptionHandler.java
package com.example.gyeol.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException e){
        Map<String,Object> body = new HashMap<>();
        body.put("message", "요청 값이 올바르지 않습니다.");
        body.put("errors", e.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of("field", fe.getField(), "error", fe.getDefaultMessage())));
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegal(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
}
