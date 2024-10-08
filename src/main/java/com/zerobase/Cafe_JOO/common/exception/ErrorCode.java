package com.zerobase.Cafe_JOO.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 컨트롤러 @PathVariable TypeMismatch
    METHOD_ARGUMENT_TYPE_MISMATCH("메서드 매개변수의 타입이 맞지 않습니다.", BAD_REQUEST),

    // Auth
    MEMBER_NOT_EXISTS("존재하지 않는 회원입니다.", BAD_REQUEST),
    EMAIL_ALREADY_EXISTS("이미 존재하는 이메일입니다.", BAD_REQUEST),
    NICKNAME_ALREADY_EXISTS("이미 존재하는 닉네임입니다.", BAD_REQUEST),
    PHONE_ALREADY_EXISTS("이미 존재하는 휴대전화번호입니다.", BAD_REQUEST),
    PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다.", BAD_REQUEST),
    ADMIN_CODE_NOT_MATCH("관리자 인증코드가 일치하지 않습니다.", BAD_REQUEST),

    // Order
    ORDER_NOT_EXISTS("존재하지 않는 주문입니다.", BAD_REQUEST),
    ORDER_ALREADY_COOKING_STATUS("이미 조리 중인 주문입니다.", BAD_REQUEST),
    ORDER_NOT_COOKING_STATUS("조리 중인 주문이 아닙니다.", BAD_REQUEST),
    ORDER_STATUS_ONLY_NEXT("주문 상태는 한 단계 다음으로만 변경이 가능합니다.", BAD_REQUEST),
    ORDER_ALREADY_CANCELED("이미 취소된 주문입니다.", BAD_REQUEST),
    ORDER_NOT_RECEIVED_STATUS("수락되지 않은 주문입니다.", BAD_REQUEST),
    ORDER_COOKING_TIME_ALREADY_SET("이미 조리 예정 시간이 설정되어 있습니다.", BAD_REQUEST),
    ORDER_NOT_ACCESS("해당 주문에 대한 접근 권한이 없습니다.", FORBIDDEN),
    START_DATE_AND_END_DATE_ARE_ESSENTIAL("시작 날짜와 끝 날짜를 입력해야 합니다.", BAD_REQUEST),

    // Product
    PRODUCT_NOT_EXISTS("존재하지 않는 상품입니다.", BAD_REQUEST),
    BEST_PRODUCT_NOT_EXISTS("베스트 상품이 존재하지 않습니다", BAD_REQUEST),

    // Cart
    CART_IS_EMPTY("장바구니에 담긴 상품이 없습니다.", BAD_REQUEST),
    CART_DOES_NOT_EXIST("존재하지 않는 장바구니입니다.",BAD_REQUEST),

    // ProductCategory
    PRODUCTCATEGORY_NOT_EXISTS("존재하지 않는 상품 카테고리입니다.", BAD_REQUEST),
    PRODUCTCATEGORY_ARLEADY_EXISTS("이미 존재하는 상품 카테고리입니다.", CONFLICT),

    // OrderProduct
    ORDER_PRODUCT_NOT_EXISTS("존재하지 않는 주문상품입니다.", BAD_REQUEST),

    // Option
    OPTION_NOT_EXISTS("존재하지 않는 옵션입니다.", BAD_REQUEST),
    OPTION_CATEGORY_NOT_EXISTS("존재하지 않는 옵션 카테고리입니다.", BAD_REQUEST),

    // Review
    REVIEW_ALREADY_WRITTEN("이미 리뷰가 작성된 주문 상품입니다.", CONFLICT),
    ORDER_PRODUCT_NOT_MATCH_MEMBER("주문하지 않은 주문 상품에 대한 리뷰 요청입니다.", FORBIDDEN);

    private final String message;
    private final HttpStatus httpStatus;
}