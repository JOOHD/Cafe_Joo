##  ##  Joo_Cafe - Exception class 분석 및 학습

### ExceptionHandler class code
    @RestControllerAdvice
    @Slf4j
    public class ExceptionHandler {
    
        // 컨트롤러 DTO validation 핸들러-yesun-23.08.21
        @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<MethodInvalidResponse> methodInvalidException(
            final MethodArgumentNotValidException e
        ) {
            BindingResult bindingResult = e.getBindingResult();
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(MethodInvalidResponse.builder()
                    .errorCode(bindingResult.getFieldErrors().get(0).getCode())
                    .errorMessage(bindingResult.getFieldErrors().get(0).getDefaultMessage())
                    .build());
        }
    
        // 컨트롤러 @PathVariable TypeMismatch 핸들러-wooyoung-23.08.24
        @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ExceptionResponse> methodArgumentTypeMismatchException() {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                    .errorCode(METHOD_ARGUMENT_TYPE_MISMATCH)
                    .errorMessage(METHOD_ARGUMENT_TYPE_MISMATCH.getMessage())
                    .build()
                );
        }
    
        // CustomException 핸들러-yesun-23.08.21
        @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
        public ResponseEntity<ExceptionResponse> customRequestException(final CustomException c) {
            log.error("Api Exception => {}, {}", c.getErrorCode(), c.getErrorMessage());
            return ResponseEntity
                .status(c.getErrorStatus())
                .body(ExceptionResponse.builder()
                    .errorCode(c.getErrorCode())
                    .errorMessage(c.getErrorMessage())
                    .build()
                );
        }
    
        @Getter
        @Builder
        public static class MethodInvalidResponse {
    
            private String errorCode;
            private String errorMessage;
        }
    
        @Getter
        @Builder
        public static class ExceptionResponse {
    
            private ErrorCode errorCode;
            private String errorMessage;
        }
    }

### 코드 분석
    ExceptionHandler class는 다양한 예외 처리를 위한 글로벌 예외 처리 클래스이다.
    각 예외가 발생했을 때 적절한 HTTP 상태 코드와 함꼐 에러 메시지를 반환.

    1. @RestControllerAdvice
        - Spring에서 글로벌 예외를 처리할 수 있게 하는 어노테이션으로,
            모든 컨트롤러에서 발생하는 예외를 이 클래스에서 처리. (JSON, XML과 같은 형태로 예외 응답을 반환).
        
        - 여러 컨트롤러에서 공통으로 처리해야 하는 예외를 한 곳에서 처리.
        - 다양한 예외 상황(유효성 검사 실패, 타입 변환 오류 등)에 대한 응답을 일관성 있게 관리
        - 클라이언트에게 일관된 에러 메시지를 전달하기 위한 JSON 응답 구조 제공
        
       @ControllerAdvice
        - view를 반환할 때 주로 사용된다.

    2. MethodArgumentNotValidException
       @ExceptionHandler(MethodArgumentNotValidException.class) 
        - 컨트롤러에서 DTO 검증 실패 시 발생하는 예외이다.
        - 예외가 발생하면 BAD_REQUEST(400) 상태 코드와 에러 코드/메시지 반환.

       @ExceptionHandler(MethodArgumentNotValidException.class)
        - @ExceptionHandler는 Spring MVC에서 특정 예외가 발생했을 때, 해당 예외를 처리할 메서드를 정의하는 어노테이션이다.        
            - 이 어노테이션은 MethodArgumentNotValidException이 발생했을 때, 해당 예외를 처리하기 위해 MethodInvalildException 메서드를 호출하도록 지정.
            
       ● @ExceptionHandler(MethodArgumentNotValidException.class) 작동 방식
        1) 클라이언트 요청 시, DTO의 필드 유효성 검사를 위해 @Valid or @Validated 어노테이션을 사용.
        2) 유효성 검사를 통과하지 못하면, methodArgumentNotValidException 예외가 발생
        3) Spring은 @ExceptionHandler(MethodArgumentNotValidException.class)가 붙은 메서드를 찾아 호출, 해당 예외를 처리.
        4) 처리된 결과는 ResponseEntity와 같은 HTTP 응답으로 클라이언트에 반환.

       ● 예외 처리 흐름.
        - DTO 유효성 검사 실패 -> methodArgumentNotValidException 발생 -> methodInvalidException 메서드가 호출 
            -> HTTP 응답으로 클라이언트에게 에러 메시지 반환. 

    3. MethodArgumentTypeMismatchException
       @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        - @PathVariable 로 전달된 값의 타입이 잘못됐을 때 발생하는 예외이다.
            ex)
                BAD_REQUEST(400) 상태 코드, 에러 코드, 에러 메시지 반환.
            
            ● enum ErrorCode class

            // 컨트롤러 @PathVariable TypeMismatch
            METHOD_ARGUMENT_TYPE_MISMATCH("메서드 매개변수의 타입이 맞지 않습니다.", BAD_REQUEST),

            // Auth
            MEMBER_NOT_EXISTS("존재하지 않는 회원입니다.", BAD_REQUEST),

            // Product
            PRODUCT_NOT_EXISTS("존재하지 않는 상품입니다.", BAD_REQUEST),


    4. CustomException
       @ExceptionHandler(CustomException.class)
        - 프로젝트에서 정의한 커스텀 예외를 처리하는 헨들러이다.
            예외 발생 시 에러 메시지를 로그로 기록하고, 클라이언트에게는 상태 코드와 에러 메시지를 반환.
        
        ● CustomException

        @Getter
        public class CustomException extends RuntimeException {
        
            private final ErrorCode errorCode;
            private final String errorMessage;
            private final HttpStatus errorStatus;
        
            public CustomException(ErrorCode errorCode) {
                super(errorCode.getMessage());
                this.errorCode = errorCode;
                this.errorMessage = errorCode.getMessage();
                this.errorStatus = errorCode.getHttpStatus();
            }
        }

    5. DTO class (MethodInvalidResponse & ExceptionResponse)
        - 예외발생 시 응답으로 사용되는 데이터 클래스이다.
            각가의 에러 정보를 담아 HTTP 응답으로 반환된다.
        
        - MethodInvalidResponse
            'DTO 검증 실패 시', 반환되는 응답 클래스이다.(ErrorCode, ErrorMessage)
            클라이언트에서 잘못된 데이터를 요청했을 때, 특히 Spring의 @Valid or @Validated를 통해,
            입력 값을 검증할 때 발상하는 응다바 객체입니다.
                    
            즉, form 데이터나 JSON 등의 입력값이 유효성 검사를 통과하지 못했을 때 반환.
            
            ex)
                ErrorCode : 유효성 검사 실패, NotNull, Size, Pattern
                ErrorMessage : 필드에서 발생한 유효성 검사 실패, '이 필드는 비어 있을 수 없습니다.', '최소 길이는 3자입니다.'

        - ExceptionResponse
            '일반적인 예외 발생 시', 반환되는 응답 클래스이다.
            ErrorCode 타입의 에러 코드와 문자열 형태의 에러 메시지를 포함.
            
            ex)
                ErrorCode : 'ORDER_NOT_FOUND', 'USER_NOT_AUTHORIZED'
                ErrorMessage : '해당 주문을 찾을 수 없습니다.', '권한이 없습니다.'
                throw new CustomException(ErrorCode.ORDER_NOT_FOUND, "해당 주문을 찾을 수 없습니다.")

        - 정리
            MethodInvalidResponse : 입력 데이터의 유효성 검증 실패와 관련된 예외 처리
            ExceptionResponse : 일반적인 예외나 비즈니스 로직 예외 처리.

### 코드 흐름

    예를 들어 회원가입 요청에서 유효하지 않은 데이터를 클라이너트가 보냈을 때, ExceptionHandler가 어떻게 동작하는지 보자.
        
    1. 회원가입 요청 (POST/signup)
        클랑이언트에서 회원가입 요청을 할 때, 필수 데이터인 email이 누락된 요청을 보낸다고 가정.
        
        ● 요청 데이터 (Invalid Data)
            {
                "name" : "John Doe",
                "email" : "", // 이메일이 비어있을 경우,
                "password" : "password123"
            }

    2. DTO 유효성 검사 설정
        UserSignupRequest 라는 DTO에 이메일 필드는 @NotEmpty 로 유효성 검사를 설정.

        ● UserSignupRequest class

            public class UserSignupRequest {
                @NotEmpty(message = "이메일은 필수 항목입니다.")
                private String email;
                
                @NotEmpty(message = "이름은 필수 항목입니다.")
                private String name;
            
                @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
                private String password;
            }
        
    3. Controller 요청 처리
        회원가입 요청을 처리하는 컨트롤러에서는 @Valid를 사용하여 유효성 검사를 실행
        
        ● UserController

            @RestController
            @RequestMapping("/api")
            public class UserController {
                
                @PostMapping("/signup")
                public ResponseEntity<String> signup(@Valid @RequestBody UserSignupRequest request) {
                    // 회원가입 처리 로직..
                    return ResponseEntity.ok("회원가입 성공");
                }
            }

    4. 유효성 검사 실패 및 예외 발생
        - 클라언트가 보낸 요청에서 email이 비어 있기 때문에 @NotEmpty 유효성 검사를 통과 못함.
        - Spring이 자동으로 MethodArgumentNotValidException 예외를 던진다.

    5. ExceptiionHanlder 작동
        - @RestControllerAdvice에 등록된 ExceptionHandler가 MethodArgumentNotValidException 예외를 잡아서 처리.
        - 이때, methodInvalidException 메서드가 호출되고, 검증에 실패에 대한 정보를 가공하여 클라이언트에 반환.

        ● methodInvalidException
        
            @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
            public ResponseEntity<MethodInvalidResponse> methodInvalidException(
            final MethodArgumentNotValidException e
            ) {
                BindingResult bindingResult = e.getBindingResult();

                // 첫 번째 오류를 가져옴 (get(0))
                FieldError fieldError = bindingResult.getFieldErrors().get(0);

                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(MethodInvalidResponse.builder()
                        .errorCode(fieldError.getCode()) // NotEmpty
                        .errorMessage(fieldError.getDefaultMessage()) // '이메일은 필수 항목입니다.'
                        .build());
            }

    6. 클라이언트에게 반환되는 데이터
        위의 methodInvalidException 메서드는 MethodInvalidResponse 객체를 반환하고, HTTP 응답으로 전달.
    
        ● 서버 응답 (Response)
            {
                "errorCode" : "NotEmpty".
                "errorMessage" : "이메일은 필수 항목입니다."
            }

    7. 흐름 요약
        1) 클라이언트가 유효하지 않은 데이터를 보내면
        2) Spring이 컨트롤러에서 유효성 검사를 수행하고, 검사 실패 시,
            MethodArgumentNotValidException 예외를 발생ㅎ시킴
        3) ExceptionHandler 가 MethodArgumentNotValidException 을 잡아서 처리.
        4) 유효성 검사 실패 정보를 클라이언트에게 반환.
                (errorCode : NotEmpty, errorMessage : '이메일은 필수 항목입니다.').
        5) 클라이언트는 반환된 에러 메시지를 보고 문제를 해결할 수 있다.

### 메서드 응답 코드
    
    ● UserDTO class

    public class UserDTO {
        
        @NotNull(message = "이메일은 필수입니다.")
        private String email;

        @Size(min = 6, message = "비밀번호는 최소 6자리여야 합니다.")
        private String password;
    }
    - UserDTO에서 email 필드가 @NotNull로 검증되었는데, 사용자가 email 값을 미입력 시, 결과 
        MethodArgumentNotValidException 발생, ExceptionHandler 처리하여 응답.

        - ErrorCode = NotNull
          ErrorMessage = '이메일은 필수입니다.'

    ● methodInvalidExceptionHandler class 

    // 컨트롤러 DTO validation 핸들러-yesun-23.08.21
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MethodInvalidResponse> methodInvalidException(
        final MethodArgumentNotValidException e
    ) {
        BindingResult bindingResult = e.getBindingResult();
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(MethodInvalidResponse.builder()
                .errorCode(bindingResult.getFieldErrors().get(0).getCode())
                .errorMessage(bindingResult.getFieldErrors().get(0).getDefaultMessage())
                .build());
    }

    ExceptionHandler 가 에러를 처리하는 과정.
    1. bindingResult.getFieldErrors().get(0).getCode()
        - bindingResult.getFieldErrors() 는 FieldError 객체들의 리스트를 반환.
        - 첫 번째 에러(get(0))의 코드(getCode())는 NotNull 이다.
        - 따라서 errorCode에 NotNull이 들어간다.
      
    2. bindingResult.getFieldErrors().get(0).getDefaultMessage();
        - 첫 번째 에러의 기본 메시지(getDefaultMessage())는 '이메일은 필수입니다.' 입니다.
        - 이 값이 errorMessage 에 들어가서 클라이언트에 전달.
### 요약
    - 이 클래스는 SpringBoot 애플리케이션에서 발생하는 예외를 처리하고, 사용자에게 일관된 에러응답을 제공.
    - 각 예외에 맞는 헨들러가 존재하며, 각 헨들러는 적절한 상태 코드와 메시지를 반환.
    - 개발자가 원하는 방식으로 예외 처리 로직을 커스터마이즈 할 수 있따.


























