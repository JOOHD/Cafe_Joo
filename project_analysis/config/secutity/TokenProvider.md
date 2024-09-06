##  Joo_Cafe - TokenProvider class 분석 및 학습

### TokenProvider class code

    @Slf4j
    @Component
    @RequiredArgsConstructor
    public class TokenProvider {
    
        private static final String KEY_ROLE = "role";
        private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24; // 1일 (24시간)
    
        private final AuthService authService;
    
        @Value("${spring.jwt.secret}")
        private String secretKey;
    
        // 토큰 생성(발급)
        public String generateToken(Long userId, String email, Role role) {
            Claims claims = Jwts.claims() // 사용자의 정보를 저장하기 위한 claim
                .setSubject(email)
                .setId(userId + "");
            claims.put(KEY_ROLE, role);
    
            Date now = new Date();
            Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);
    
            return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 생성 시간
                .setExpiration(expiredDate) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, secretKey) // 사용할 암호화 알고리즘, 시크릿 키
                .compact();
        }
    
        // jwt 에서 인증정보 추출
        public Authentication getAuthentication(String token) {
            UserDetails userDetails = authService.loadUserByUsername(getEmail(token));
            List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
            return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
        }
    
        // 토큰에서 사용자 이메일 추출
        public String getEmail(String token) {
            return parseClaims(token).getSubject();
        }
    
        // 토큰에서 사용자 id 추출
        public Long getId(String token) {
            return Long.parseLong(parseClaims(removeBearerFromToken(token)).getId());
        }
    
        // 토큰 유효성검사
        public boolean validateToken(String token) {
            if (!StringUtils.hasText(token)) {
                return false;
            }
    
            Claims claims = parseClaims(token);
            return !claims.getExpiration().before(new Date());
        }
    
        // 토큰에서 클레임 정보 추출
        private Claims parseClaims(String token) {
            try {
                return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            } catch (ExpiredJwtException e) {
                log.warn("만료된 JWT 토큰입니다.");
                return e.getClaims();
            }
        }
    
        // 토큰 인증 타입 제거
        private String removeBearerFromToken(String token) {
            return token.substring(JwtAuthenticationFilter.TOKEN_PREFIX.length());
        }
    }

### 코드 분석

    ● 필드 설명
        - KEY_ROLE : JWT에서 사용자의 역할을 저장할 key 값이다.
        - TOKEN_EXPIRE_TIME : 토큰의 유효 기간을 1일(24시간)로 설정합니다.
        - authService : 사용자 정보를 조회하기 위한 서비스입니다.
        - secretKey : JWT 토큰을 암호화할 때 사용하는 비밀 key입니다.

    ● @Value("${spring.jwt.secret}")
        - 외부 설정 파일(application.properties) 같은 파일에서 값을 가져와서, 
            해당 필드에 주입하는 역할이다.

            ex)
                application.properties 예를 들자면,
                spring.jwt.secret = mySecretKey

                ${spring.jwt.secret} = mySecretKey 값을 가져온다.
                secretKey = JWT 토큰을 서명 및 검증에 사용하는 비밀 키.

    ● 메서드 설명
        1. generateToken(Long userId, String email, Role role)
            - JWT 토큰을 생성하는 목적의 메서드.
            - Claim claim : 사용자의 정보(email, role)를 담은 객체, 여기서
                                setSubject(email)로 이메일을, setId(userId)로 사용자 ID를 설정.

                ● Claim 이란?
                    - JWT(Jason Web Token)에서 사용자의 정보나 추가 데이터를 담는 부분.
                        JWT는 크게 세 부분으로 나뉘어 있는데, 각각 Header, Payload, signature로 구성된다.
                        그 중 Payload가 바로 Claim을 담고 있는 부분이다.
                        - Claim은 사용자의 데이터(email, auth, id등)를 토큰에 저장할 수 있게 해준다.
                        - JWT는 Claim을 기반으로 사용자의 정보를 안전하게 교환.
                        - Claim은 주로 두 가지로 나뉜다.
                            1. 등록된 클레임
                                subject, issuer, expireation, time등 사전에 정의된 클레임
                            2. 비공식 클레임
                                개발자가 필요에 따라 추가하는 클레임, role, userId 같은

                    - Token을 String으로 선언하는 이유
                        - JWT가 기본적으로 Base64로 인코딩된 문자열.

            - Date expireDate : 토큰의 만료 시간을 현재 시간으로부터 24시간 후로 설정.
            - Jwts.builder() : JWT 토큰을 생성, 여기서 사용자 정보, 생성 시간, 만료 시간, 
                                암호화 알고리즘과 비밀 키를 설정하고 토큰을 최종적으로 만든다.

        2. getAuthentication(String token)
            - JWT 토큰에서 인증 정보를 추출하는 메서드.
            - 토큰에서 사용자 이메일을 추출하여 authService로 사용자 정보를 로드하고, 이를 바탕으로 
                UsernamePasswordAuthenticationToken 을 생성하여 반환.

        3. getEamil(String token)
            - JWT 토큰에서 사용자의 이메일을 추출하는 메서드.
            - 토큰에서 parseClaims()를 통해 클레임 정보를 추출하고, getId()를 호출하여 사용자의 ID를 반환.
        
        4. validateToken(String token)
            - JWT 토큰의 유효성을 검사하는 메서드.

        5. parseClaims(String token)
            - JWT 토큰을 해석하고, 유효하면 클레임을 반환, 만약 토큰이 만료되었다면
                ExpiredJwtException 을 던지며, 만료된 클레임을 반환.

        6. removeBearerFromToken(String token)
            - 토큰에서 Bearer 라는 인증 타입을 제거하는 메서드아디.
                JWT 토큰 앞에 붙어있는 Bearer 문자열을 제거하고 실제 토큰만 추출한다.

### 코드 흐름
        
    1. 사용자가 로그인 시, generateToken() 메서드로 JWT 토큰을 생성하여 발급.
    2. 클라이언트는 이후 요청 시 헤더에 이 JWT 토큰을 포함하여 서버에 요청을 보낸다.
    3. 서버는 요청을 받을 때 getAuthentication()을 통해 토큰을 해석하고, 사용자의 인증 정보를 추출.   
    4. 추출한 정보를 통해 사용자 인증을 처리하고, 필요한 경우 validateToken()으로 토큰의 유효성을 검사한다.










