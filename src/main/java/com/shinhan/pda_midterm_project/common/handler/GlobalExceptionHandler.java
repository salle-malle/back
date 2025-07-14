package com.shinhan.pda_midterm_project.common.handler;

import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.domain.auth.exception.AuthException;
import com.shinhan.pda_midterm_project.domain.member.exception.MemberException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<Response<String>> handleMemberException(MemberException ex) {
        return ResponseEntity
                .badRequest()
                .body(Response.failure(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<String>> handleDtoException(MethodArgumentNotValidException ex) {
        // TODO: 리팩토링 해야함
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst().map(FieldError::getDefaultMessage)
                .orElse("형식 예외가 발생하였습니다.");

        return ResponseEntity
                .badRequest()
                .body(Response.failure("FORMAT-001", errorMessage));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Response<String>> handleAuthException(AuthException ex) {
        return ResponseEntity
                .badRequest()
                .body(Response.failure(ex.getCode(), ex.getMessage()));
    }
}
