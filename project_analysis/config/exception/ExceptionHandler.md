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
            모든 컨트롤러에서 발생하는 예외를 이 클래스에서 처리.

    2. MethodArgumentNotValidException
       @ExceptionHandler(MethodArgumentNotValidException.class)
        - 컨트롤러에서 DTO 검증 실패 시 발생하는 예외이다.
        - 예외가 발생하면 BAD_REQUEST(400) 상태 코드와 에러 코드/메시지 반환.

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
        
        - methodInvalidResponse
            DTO 검증 실패 시 반환되는 응답 클래스이다.(ErrorCode, ErrorMessage)
    
        - ExceptionResponse
            일반적인 예외 발생 시 반환되는 응답 클래스이다.
            ErrorCode 타입의 에러 코드와 문자열 형태의 에러 메시지를 포함.

### 요약
    - 이 클래스는 SpringBoot 애플리케이션에서 발생하는 예외를 처리하고, 사용자에게 일관된 에러응답을 제공.
    - 각 예외에 맞는 헨들러가 존재하며, 각 헨들러는 적절한 상태 코드와 메시지를 반환.
    - 개발자가 원하는 방식으로 예외 처리 로직을 커스터마이즈 할 수 있따.


























