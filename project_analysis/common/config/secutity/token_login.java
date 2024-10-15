/*
    cookie/session 인증 방식을 사용할 경우, API 서버로 요청을 보낸 사용자에 대한 정보를 조회하기 위해,
    header에 담겨온 sessionId로 session을 조회해아 한다.
    이는 결국 session에 대한 접근을 해야 하는 소요가 발생해 트래픽이 집중되는 경우 성능에 영향을 미칠 수 있다.

    이러한 문제를 Jwt 토큰 인증 방식을 사용하면서 크게 해결된다.
    Jwt 토큰은 토큰 안에 요청을 보낸 클라이언트에 대한 정보가 담을 수 있기 때문이다.

    토큰에 클라이언트가 누군지에 대해 식별할 수 있으며, 외부에 노출될 경우 큰 문제가 되지 않을 정보들을
    토큰에 넣어두고 이를 헤더에 담아 요청과 함께 받는다면, 서버 입장에서도 DB에 접근없이 클라이언트 정보를
    조회할 수 있다는 점이 Jwt 인증 방식의 장점이다.

    그래서 클라이언트에 대한 id, email, name 등과 같은 기본적인 인적 정보를 token PayLoad에 넣어두는 것이 일반적이다.

    authController 로그인 시, return 값 SigninForm.Response token 전달
*/

public class TokenProvider { // token 생성/검증/인증

    private static final String KEY_ROLE = "role";
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;

    @Value("${spring.jwt.secret")
    private String secretKey;

    // 토큰 생성(발급)
    public String generateToken(Long userId, String email, Role role) {
        Claims claims = Jwts.claims() // 사용자 정볼르 저장하기 위한 claim
                .setSubject(email)        // 토큰 제목
                .setId(userId + "");
        claims.put(KEY_ROLE, role);

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(clasims)
                .setIssuedAt(now)           // 토큰 생성 시간
                .setExpiration(expiredDate) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, secretKey) // 사용할 암호화 알고리즘, 시크릿 키
                .compact(); // encoding 된 Header, Payload, Signature를 (.)으로 구분하여 하나의 문자열로 결합.
    }
    
    /*
        UserDetails 란?
        Spring Security에서 사용자의 정보를 담는 인터페이스이다. (@Override 메서드를 이용)

        여러 메서들 중, getUsername() 이 중요하다.
        username은 계저으이 고유한 값인데 몇몇 코드에서는 email(로그인용 아이디)을 넘겨준다.
        하지만 email은 SSO 같은 서버를 만들게 되면 정책에 따라서 중복이 될 수도 있기 때문에
        보통 DB에서 User Table에 PK 값을 넘겨준다.
     */

    // jwt 에서 인증정보 추출
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = authService.loadUserByUsername(getEmail(token));
        List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
        return new UsernamePasswordAuthenticationToken(userDetails. "", authorities);
    }

    // token 사용자 email 추출
    public String  getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    // token 사용자 id 추출
    public Long getId(String token) {
        return Long.parseLong(parseClaims(removeBearerFromToken(token)).getId());
    }

    // token validation
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        Claims claims = parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    // token claim 정보 추출
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다.");
            return e.getcClaims();
        }
    }

    // token 인증 타입 제거
    private String removeBearerFromToken(String token) {
        return token.substring(JwtAuthenticationFilter.TOKEN_PREFIX.length());
    }
}

// token 생성 - 전달 - 반환 과정

// 클라이언트에서 사용자가 입력한 데이터 전송/전달 클새스
public class SigninForm {

    @ApiModel(value = "SigninFormRequest")
    @Getter
    public static class Reqeust { // Request (사용자 입력 값) = email, password
        @Schema(description = "이메일", example = "test@cafe.com")
        @Email(message = "이메일 형식을 확인해주세요.")
        private String email;

        @Shema(description = "비밀번호", example = "test123!")
        @NotBlank(message = "비밀번호는 필수로 입력해야 합니다.")
        private String password;
    }

    // 클라이언트가 로그인 요청을 보내면, 서버는 사용자가 인증에 성공했을 때 token(JWT)을 발듭하고 이를 클라이언트에 반환
    @ApiModel(value = "SigninFormResponse")
    @Getter
    @Builder
    public static class Response { // 서버 -> 클라이언트 = token : 사용자의 인증 상태를 유지하기 위해서.
        @Schema(description = "토큰", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxFX1VTR0.mhuc2GGHEEJwZi")
        private String token;

        public static Response from(String token) {
            return Response.builder().token(token).build();
        }
    }
}

public class SiginDto { // 클라이언트에서 받은 form 데이터에서 필요한 데이터를 dto에 가져옴

    @Getter
    @Builder
    public static class Request {

        private String email;
        private String password;
        
        public static SigninDto.Request from(SigninForm.Request signinForm) {
            return Request.builder() // SigininForm 클래스에서 get 메서드를 통해 필요한 데이터 가져옴
                    .email(signinForm.getEmail())
                    .password(signinForm.getPassword())
                    .build();        // 객체 생성       
        }
    }

    @Getter
    @Builder
    public static class Response {
    
        // 서버에서 생성된 값(memberId, role)
        private Long memberId;
        private String email;
        private Role role;

        // return으로 반환된 값은 서버간의 데이터 전송을 위한 데이터를 가공하여 전달하기 위함.
        public static Response from(Member member) {
            return Response.builder()
                    .memberId(member.getId())
                    .email(member.getEmail())
                    .role(member.getRole())
                    .build();
        }
    }
}

@Tag(name = "auth-controller", description = "회원가입, 로그인 API")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenProvider;

    @Value("${admin.code}") // application.yml 데이터 가져오기
    private String ADMIN_CODE;

    @ApiOperation(value = "사용자, 관리자 공통 로그인", notes = "사용자와 관리자 모두 이메일, 비밀번호로 로그인합니다.")
    @PostMapping("/signin")
    public ResponseEntity<SigninForm.Response> signin(
            @RequestBody @Valid SigninForm.Request signinForm
    ) {
        // Response signinDto = authService.sigin(signinForm);
        authService.signin(SigninDto.Response from(signinForm));
        String token = tokenProvider.generateToken(
                signinDto.getMemberId(), signinDto.getEmail(), sigininDto.getRole()
        );
        return ResponseEntity.ok(SigninForm.Response.from(token));
    }
}

public class AuthService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).
                orElseThrow(() -< new CustomException(MEMBER_NOT_EXISTS));

        lIST<GrantedAuthority> grantedAuthorities = new ArrayList<>();

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




