package com.example.gyeol.exception;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        FieldError fe = ex.getBindingResult().getFieldError();
        String msg = (fe != null) ? fe.getDefaultMessage() : "요청 형식이 올바르지 않습니다.";
        return error(HttpStatus.BAD_REQUEST, "Bad Request", msg, req);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<?> handleBadRequest(Exception ex, HttpServletRequest req) {
        return error(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), req);
    }

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class, JwtException.class})
    public ResponseEntity<?> handleUnauthorized(Exception ex, HttpServletRequest req) {
        return error(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage(), req);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleForbidden(AccessDeniedException ex, HttpServletRequest req) {
        return error(HttpStatus.FORBIDDEN, "Forbidden", "권한이 없습니다.", req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleEtc(Exception ex, HttpServletRequest req) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "예기치 못한 오류가 발생했습니다.", req);
    }

    private ResponseEntity<Map<String, Object>> error(HttpStatus status, String error, String message, HttpServletRequest req) {
        return ResponseEntity.status(status).body(Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "error", error,
                "message", message,
                "path", req.getRequestURI()
        ));
    }
}
