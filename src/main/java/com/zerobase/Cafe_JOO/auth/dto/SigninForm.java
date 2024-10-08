package com.zerobase.Cafe_JOO.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class SigninForm {

    @ApiModel(value = "SigninFormRequest")
    @Getter
    public static class Request {

        @Schema(description = "이메일", example = "test@cafe.com")
        @Email(message = "이메일 형식을 확인해주세요.")
        private String email;

        @Schema(description = "비밀번호", example = "test123!")
        @NotBlank(message = "비밀번호는 필수로 입력해야 합니다.")
        private String password;
    }

    @ApiModel(value = "SigninFormResponse")
    @Getter
    @Builder
    public static class Response {

        @Schema(description = "토큰", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxFX1VTR0.mhuc2GGHEEJwZi")
        private String token;

        public static Response from(String token) {
            return Response.builder().token(token).build();
        }
    }

}
