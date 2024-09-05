## Joo_Cafe - SwaggerConfig class 분석 및 학습

### SwaggerConfig class code

    @Configuration
    @EnableSwagger2
    public class SwaggerConfig implements WebMvcConfigurer {
    
        @Bean
        public Docket api() {
            return new Docket(DocumentationType.OAS_30)
                    .securityContexts(Arrays.asList(securityContext()))
                    .securitySchemes(Arrays.asList(apiKey()))
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.zerobase.joo_Cafe"))
                    .paths(PathSelectors.any())
                    .build()
                    .apiInfo(apiInfo())
                    .ignoredParameterTypes(Errors.class);
        }
    
        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .title("Joo_Cafe Api Test")
                    .description("Joo_Cafe REST API입니다.<br>"
                            + "<br>"
                            + "권한이 필요한 기능은 로그인 API로 로그인 후 받은 토큰으로 테스트가 가능합니다. <br>"
                            + "토큰 입력 방법 : **Bearer {받은 토큰}**  <br>"
                            + "<br>"
                            + "관리자 기능 : 토큰을 우측 초록색 Authorize 버튼에만 넣으면 됩니다.<br> "
                            + "사용자 기능 : 토큰을 Authorize 버튼과 API 헤더 Authorize 두 군데에 넣어야합니다.<br>  ")
                    .version("1.0.2")
                    .contact(new Contact("Joo_Cafe GitHub", "https://github.com/JOOHD/Joo_Cafe", ""))
                    .build();
        }
    
        // method
        // swagger-ui 페이지 연결 핸들러 설정
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry
                    .addResourceHandler("/swagger-ui.html")
                    .addResourceLocations("classpath:/META-INF/resources/");
            registry
                    .addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    
        // swagger Authorize 버튼 생성을 위한 기능
        private SecurityContext securityContext() {
            return SecurityContext.builder()
                    .securityReferences(defaultAuth())
                    .build();
        }
    
        // swagger Authorize 버튼 생성을 위한 기능
        private List<SecurityReference> defaultAuth() {
            AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
            authorizationScopes[0] = new AuthorizationScope("global", "accessEverything");
            return List.of(new SecurityReference("Authorization", authorizationScopes));
        }
    
        // swagger Authorize 버튼 생성을 위한 기능
        private ApiKey apiKey() {
            return new ApiKey("Authorization", "Authorization", "header");
        }
    }

### 클래스 코드 분석
    위 클래스는 Swagger를 설정하는 구성 클래스이다.
    Swagger는 API 문서를 자동으로 생성하고 API를 테스트할 수 있는 UI를 제공하는 도구.
    SwaggerConfig 클래스는 Spring Boot의 Swagger2를 활성화하여, 애플리케이션의 REST API에 대한 문서를 생성하고, 웹 UI로 제공.

    ● Swagger 설정 클래스 선언
        1. @Configuration
            - 이 클래스가 스프링의 설정 파일임을 나타낸다.
            
        2. @EnableSwagger2 
            - Swagger2를 활성화한다. 

        3. WebMvcConfigurer 
            - Swagger UI 리소스를 설정하는데 사용된다.

    ● Docket Bean 설정
        1. Docket 
            - 객체는 Swagger의 핵심 객체로, API 문서를 생성하는 설정을 담고 있다.
        
        2. securityContexts & securitySchemes
            - Swagger UI에서 인증 토큰을 입력할 수 잇도록 보안 관련 설정을 맡고 있다.
                여기서  apikey() 를 통해 header에 Authoriztion 이라는 키로 토큰을 전달받는다.

        3. apis(RequestHandlerSelectors.basePachage("com.zerobase.Joo_Cafe"))
            - 이 패키지 안에 있는 클래스들에 대한 API 문서를 생성하겠다.

        4. .path(PathSelectors.any())
            - 모든 경로에 대해 문서를 생성한다.

        5. ignoredParameterTypes(Errors.class) 
            - Errors 클래스는 Swagger 문서에 포함하지 않도록 설정.

    ● API 정보 설정 (apiInfo() 메서드)
        - API 문서의 제목, 설명, 버전, 연락처 등을 설정한다.
        - 설명에서, Bearer 토큰을 사용하여 API에 접근하는 방법을 나타내고 있다.
        
    ● ResourceHandler 설정(addResourceHandlers))
        - Swagger UI와 웹 자원 경로를 설정
        - /swagger-ui.html : Swagger UI에 접근할 수 있는 경로를 설정.
        - /webjars/** : Swagger UI 관련 자바스크립트 및 CSS 리소스들을 로드하기 위한 경로.

    ● Authorize button
        - securityContext() 
            - defaultAuth(), apiKey() 메서드를 통해 Swagger UI에서 API 호출 시 사용할 인증 방식을 설정한다.

        - ApiKey 
            - API 키 인증 방식을 정의하며, 여기서는 HTTP 헤더의 Authorization 키에 토큰을 넣어 전달하는 방식이다.

        - securityReference
            - 인증 범위를 지정한다. 여기서는 글로벌 범위로 설정하여 모든 API에 접근할 수 있도록 한다.

    ● Swagger UI, Bearer Token 인증
        - Swagger UI에서 Authorize 버튼을 통해 사용자가 발급받은 Bearer 토큰을 입력할 수 있게하고,
            이 토큰은 API 호출 시, HTTP 헤더에 추가.

            ex)
                POST/admin/order/status/{orderId}
                Authorization: Bearer <my-token>

            - Swagger UI에서 authorize 버튼을 눌러 토큰을 입력한 후, 각 API 요청에 자동으로 토큰이 헤더에 포함되어 전송

        
                
            