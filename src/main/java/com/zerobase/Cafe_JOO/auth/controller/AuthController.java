package com.zerobase.Cafe_JOO.auth.controller;

import com.zerobase.Cafe_JOO.auth.dto.SigninDto.Response;
import com.zerobase.Cafe_JOO.auth.dto.SigninForm;
import com.zerobase.Cafe_JOO.auth.dto.SignupAdminForm;
import com.zerobase.Cafe_JOO.auth.dto.SignupMemberForm;
import com.zerobase.Cafe_JOO.auth.service.AuthService;
import com.zerobase.Cafe_JOO.common.config.security.TokenProvider;
import com.zerobase.Cafe_JOO.common.exception.CustomException;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.zerobase.Cafe_JOO.common.exception.ErrorCode.ADMIN_CODE_NOT_MATCH;
import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "auth-controller", description = "회원가입, 로그인 API")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenProvider;

    @Value("${admin.code}")
    private String ADMIN_CODE;

    @ApiOperation(value = "사용자 회원가입", notes = "일반 사용자는 이메일, 닉네임, 전화번호, 비밀번호로 회원가입합니다.")
    @PostMapping("/signup")
    public ResponseEntity<Void> memberSignup(
            @RequestBody @Valid SignupMemberForm.Request signupMemberForm
    ) {
        authService.signup(signupMemberForm);
        return ResponseEntity.status(CREATED).build();
    }

    @ApiOperation(value = "관리자 회원가입", notes = "관리자는 관리자 인증코드, 이메일, 비림번호로 회원가입합니다.")
    @PostMapping("/signup/admin")
    public ResponseEntity<Void> adminSignup(
        @RequestBody @Valid SignupAdminForm.Request signupAdminForm
    ) {
        if (!ADMIN_CODE.equals(signupAdminForm.getAdminCode())) {
            throw new CustomException(ADMIN_CODE_NOT_MATCH);
        }
        authService.signup(signupAdminForm);
        return ResponseEntity.status(CREATED).build();
    }

    @ApiOperation(value = "사용자, 관리자 공통 로그인", notes = "사용자와 관리자 모두 이메일, 비밀번호로 로그인합니다.")
    @PostMapping("/signin")
    public ResponseEntity<SigninForm.Response> signin(
        @RequestBody @Valid SigninForm.Request signinForm
    ) {
        // authService.signin(SigninDto.Response from(signinForm));
        Response signinDto = authService.signin(signinForm);
        String token = tokenProvider.generateToken(
            signinDto.getMemberId(), signinDto.getEmail(), signinDto.getRole()
        );
        return ResponseEntity.ok(SigninForm.Response.from(token));
    }
}





























