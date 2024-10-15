package com.zerobase.Cafe_JOO.front.order.service.impl;

import com.zerobase.Cafe_JOO.common.config.security.TokenProvider;
import com.zerobase.Cafe_JOO.common.exception.CustomException;
import com.zerobase.Cafe_JOO.common.type.OrderCookingStatus;
import com.zerobase.Cafe_JOO.common.type.OrderReceiptStatus;
import com.zerobase.Cafe_JOO.front.cart.domain.Cart;
import com.zerobase.Cafe_JOO.front.cart.domain.CartOption;
import com.zerobase.Cafe_JOO.front.cart.domain.CartOptionRepository;
import com.zerobase.Cafe_JOO.front.cart.domain.CartRepository;
import com.zerobase.Cafe_JOO.front.member.domain.Member;
import com.zerobase.Cafe_JOO.front.member.domain.MemberRepository;
import com.zerobase.Cafe_JOO.front.order.domain.*;
import com.zerobase.Cafe_JOO.front.order.dto.OrderAddDto;
import com.zerobase.Cafe_JOO.front.product.domain.OptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.zerobase.Cafe_JOO.common.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;

    private final CartRepository cartRepository;
    private final CartOptionRepository cartOptionRepository;

    private final OptionRepository optionRepository;

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderProductOptionRepository orderProductOptionRepository;

    private final TokenProvider tokenProvider;

    // 주문 수락 시간 저장
    public LocalDateTime savaReceivedTime(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ORDER_NOT_EXISTS));

        if (order.getCookingStatus() != OrderCookingStatus.COOKING) {
            throw new CustomException(ORDER_NOT_COOKING_STATUS);
        }

        /*
            // 주문 수락  시간 코드
            if (this.cookingStatus == OrderCookingStatus.NONE
                    && newStatus == OrderCookingStatus.COOKING) {
                this.receivedTime = LocalDateTime.now();
            }
        */
        return order.getReceivedTime();
    }

    // 주문 경과 시간 계산
    public Long findElapsedTime(String token, Long orderId) {
        Long userId = tokenProvider.getId(token);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ORDER_NOT_EXISTS));

        if (!order.getMember().getId().equals(userId)) {
            throw new CustomException(ORDER_NOT_ACCESS);
        }

        if (order.getCookingStatus() != OrderCookingStatus.COOKING) {
            throw new CustomException(ORDER_NOT_COOKING_STATUS);
        }

        LocalDateTime receivedTime = order.getReceivedTime();;
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(receivedTime, currentTime);

        return duration.toMinutes();
    }

    // 주문 취소
    public void modifyOrderCancel(String token, Long orderId) {
         Long userId = tokenProvider.getId(token);

         Order order = orderRepository.findById(orderId)
                 .orElseThrow(() -> new CustomException(ORDER_NOT_EXISTS));

        // 주문자가 맞는지 확인, tokenProvider.getId(token)를 통해 토큰에서 사용자 ID를 추출하고,
        // 주문의 사용자 ID와 비교하여 권한을 확인한다, 다른 사용자가 자신의 주문을 취소 못하도록
         if (!order.getMember().getId().equals(userId)) {
             throw new CustomException(ORDER_NOT_ACCESS);
         }

        // 주문이 이미 진행 중인지 확인(COOKING, ReceiptStatus.RECEIVED)에서는 취소 불가.
         if (order.getCookingStatus() == OrderCookingStatus.COOKING
            || order.getReceiptStatus() == OrderReceiptStatus.RECEIVED) {
             throw new CustomException(ORDER_ALREADY_COOKING_STATUS);
         }

         // 이미 취소된 주문인지 확인.(CANCELED, REJECTED 된 상태라면 중복 취소를 방지)
         if (order.getReceiptStatus() == OrderReceiptStatus.CANCELED
            || order.getReceiptStatus() == OrderReceiptStatus.REJECTED) {
             throw new CustomException(ORDER_ALREADY_CANCELED);
         }

         // 모든 조건을 만족했을 경우, 주문을 취소할 수 있도록 처리, 주문 상태를 CANCELED로 변경한 뒤,
         order.modifyReceiptStatus(OrderReceiptStatus.CANCELED);

         // DB에 저장.
         orderRepository.save(order);
    }

    // 주문 생성
    @Transactional
    public void addOrder(String token, OrderAddDto.Request orderAddDto) {
        Long userId = tokenProvider.getId(token);
        Member memberById = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_EXISTS));

        Order savedOrder = orderRepository.save(Order.builder()
                .member(memberById)
                .payment(orderAddDto.getPayment())
                .cookingStatus(OrderCookingStatus.NONE)
                .receiptStatus(OrderReceiptStatus.WAITING)
                .build());

        // Member Entity 로 사용자 장바구니 전체 조회
        List<Cart> cartListByMember = cartRepository.findAllByMember(memberById);
        if (cartListByMember.isEmpty()) {
            throw new CustomException(CART_IS_EMPTY);
        }

        // Cart Entity 로 장바구니_옵션 테이블 조회
        cartListByMember.forEach(cart -> {
            // 장바구니 테이블의 상품 정보들을 주문_상품 테이블에 저장
            orderProductRepository.save(OrderProduct.builder()
                    .orderId(savedOrder.getId())
                    .product(cart.getProduct())
                    .build());
            
            // 장바구니_옵션 테이블의 옵션 정보들을 주문_상품_옵션 테이블에 저장
            List<CartOption> cartOptionList = cartOptionRepository.findAllByCart(cart);
            cartOptionList.forEach(cartOption ->
                orderProductOptionRepository.save(
                    OrderProductOption.builder()
                            .orderProductId(Long.valueOf(cart.getProduct().getId()))
                            .option(optionRepository.findById(cartOption.getOption().getId())
                                    .orElseThrow(() -> new CustomException(OPTION_NOT_EXISTS)))
                            .build())
            );
        });

        // 장바구니 테이블에서 사용자의 모든 장바구니 정보 삭제
        // cartRepository.deleteAllByMember(memberById);

    }
}
