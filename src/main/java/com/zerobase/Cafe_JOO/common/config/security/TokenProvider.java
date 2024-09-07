package com.zerobase.Cafe_JOO.common.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String KEY_ROLE = "role";
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;//

    private final AuthService authService;

    @Value("${spring.jwt.secret")
    private String secretKey;

    // 토큰 생성(발급)
    public String generateToken(Long userId, String email, Role role) {
        Claims claims = Jwts.claims() // 사용자의 정보를 저장하기 위한 claim
                .setSubject(email)    // 토큰의 주제를 사용자 이메일로 설정
                .setId(userId + "");  // 사용자 ID를 문자열로 설정
        claims.put(KEY_ROLE, role);   // 사용자 역할을 클레임에 추가
        
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);
        
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 생성 시간
                .setExpiration(expiredDate) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, secretKey) // 사용할 암호화 알고리즘, 토큰 서명
                .compact();
    }

    // jwt 에서 인증정보 추출
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = authService.loadUserByUsername(getEmail(token));
        List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities); // 인증 정보 반환
    }

    // 토큰에서 사용자 이메일 추출
    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    // 토큰에서 사용자 id 추출
    public Long getId(String token) {
        return Long.parseLong(parseClaims(removeBearerFromToken(token)).getId());
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        Claims claims = parseClaims(token);
        return !claims.getExpiration().before(new Date()); // 토큰의 만료 시간이 현재 시간보다 이후인지 확인.
    }
    
    // 토큰에서 클레임 정보 추출
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {    // 토큰이 만료된 경우,
            log.warn("만료된 JWT 토큰입니다."); // 경고 로그
            return e.getClaims();
        }
    }

    // 토큰 인증 타입 제거(토큰 문자열에서 "Bearer " 접두사를 제거, 순수한 JWT 토큰만 추출)
    private String removeBearerFromToken(String token) {
        // TOKEN_PREFIX는 일반적으로 "Bearer" 로 설정한다.
        return token.substring(JwtAuthenticationFilter.TOKEN_PREFIX.length());
    }
}
