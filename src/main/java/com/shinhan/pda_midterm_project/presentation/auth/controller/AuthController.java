package com.shinhan.pda_midterm_project.presentation.auth.controller;

import com.shinhan.pda_midterm_project.common.annotation.Auth;
import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import com.shinhan.pda_midterm_project.domain.auth.service.AuthService;
import com.shinhan.pda_midterm_project.domain.auth.service.TokenCookieManager;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.service.MemberService;
import com.shinhan.pda_midterm_project.presentation.auth.dto.request.AuthRequest;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final MemberService memberService;

	@PostMapping("/login")
	public ResponseEntity<Response<Object>> login(
			@Valid @RequestBody AuthRequest.Login loginRequest) {
		String id = loginRequest.id();
		String password = loginRequest.password();
		ResponseCookie responseCookie = authService.login(id, password);

		return ResponseEntity
				.ok()
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.body(Response.success(
						ResponseMessages.LOGIN_SUCCESS.getCode(),
						ResponseMessages.LOGIN_SUCCESS.getMessage(),
						Map.of("userIsLogin", true)
				));
	}

	@PostMapping("/signup")
	public ResponseEntity<Response<Object>> signUp(
			@Valid @RequestBody AuthRequest.SignUp signUpRequest) {
		String id = signUpRequest.id();
		String password = signUpRequest.password();
		String phoneNumber = signUpRequest.phoneNumber();
		String appKey = signUpRequest.appKey();
		String appSecret = signUpRequest.appSecret();
		String accountNumber = signUpRequest.accountNumber();
		ResponseCookie responseCookie = authService.signUp(id, password, phoneNumber, appKey, appSecret, accountNumber);

		return ResponseEntity
				.ok()
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.body(Response.success(
						ResponseMessages.SIGNUP_SUCCESS.getCode(),
						ResponseMessages.SIGNUP_SUCCESS.getMessage()));
	}

	@GetMapping("/verify")
	public ResponseEntity<Response<Object>> verifyToken(@Auth Accessor accessor) {
		if (!accessor.isMember()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Response.failure("401", "인증이 필요합니다."));
		}
		return ResponseEntity.ok(Response.success("200", "인증된 사용자입니다."));
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout() {
		return ResponseEntity
				.ok()
				.header(HttpHeaders.SET_COOKIE, TokenCookieManager.deleteCookie().toString())
				.build();
	}

}
