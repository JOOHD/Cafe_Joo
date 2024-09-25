package com.zerobase.Cafe_JOO.auth.service;

import com.zerobase.Cafe_JOO.auth.dto.*;
import com.zerobase.Cafe_JOO.common.exception.CustomException;
import com.zerobase.Cafe_JOO.front.member.domain.Member;
import com.zerobase.Cafe_JOO.front.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.zerobase.Cafe_JOO.common.config.security.Role.ROLE_ADMIN;
import static com.zerobase.Cafe_JOO.common.config.security.Role.ROLE_USER;
import static com.zerobase.Cafe_JOO.common.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    // 사용자 회원가입
    public void signup(SignupMemberForm.Request form) {
        SignupDto dto = SignupDto.from(form); // SignupMemberForm -> SignupDto from(transfer)
        if (memberRepository.findByNickname(dto.getNickname()).isPresent()) {
            throw new CustomException(NICKNAME_ALREADY_EXISTS);
        }
        if (memberRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new CustomException(EMAIL_ALREADY_EXISTS);
        }
        memberRepository.save(
                Member.from(dto, passwordEncoder.encode(dto.getPassword()), ROLE_USER));
    }

    // 관리자 회원가입
    public void signup(SignupAdminForm.Request form) {
        SignupDto dto = SignupDto.from(form);

        memberRepository.findByEmail(dto.getEmail()).ifPresent(member -> {
            throw new CustomException(EMAIL_ALREADY_EXISTS);
        });

        memberRepository.save(
            Member.from(dto, passwordEncoder.encode(dto.getPassword()), ROLE_ADMIN));
    }

    // 공통 로그인
    public SigninDto.Response signin(SigninForm.Request form) {
        Member member = memberRepository.findByEmail(form.getEmail())
            .orElseThrow(() -> new CustomException(MEMBER_NOT_EXISTS));

        if (!passwordEncoder.matches(form.getPassword(), member.getPassword())) {
            throw new CustomException(PASSWORD_NOT_MATCH);
        }

        return SigninDto.Response.from(member);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(MEMBER_NOT_EXISTS)
        );

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        if (member.getRole().toString().equals(ROLE_ADMIN.toString())) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (member.getRole().toString().equals(ROLE_USER.toString())) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .authorities(grantedAuthorities)
                .build();
    }
}















