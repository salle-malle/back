package com.shinhan.pda_midterm_project.presentation.auth.controller;

import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.model.UserTokens;
import com.shinhan.pda_midterm_project.domain.auth.service.AuthService;
import com.shinhan.pda_midterm_project.presentation.auth.dto.request.AuthRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Response<Object>> login(
            @Valid @RequestBody AuthRequest.Login loginRequest
    ) {
        String id = loginRequest.id();
        String password = loginRequest.password();
        UserTokens loginResult = authService.login(id, password);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, loginResult.accessToken().toString())
                .body(Response.success(
                        ResponseMessages.LOGIN_SUCCESS.getCode(),
                        ResponseMessages.LOGIN_SUCCESS.getMessage()
                ));
    }

    @PostMapping("/signup")
    public ResponseEntity<Response<Object>> signUp(
            @Valid @RequestBody AuthRequest.SignUp signUpRequest
    ) {
        String id = signUpRequest.id();
        String password = signUpRequest.password();
        String phoneNumber = signUpRequest.phoneNumber();
        UserTokens userTokens = authService.signUp(id, password, phoneNumber);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, userTokens.accessToken().toString())
                .body(Response.success(
                        ResponseMessages.SIGNUP_SUCCESS.getCode(),
                        ResponseMessages.SIGNUP_SUCCESS.getMessage()
                ));
    }
}
