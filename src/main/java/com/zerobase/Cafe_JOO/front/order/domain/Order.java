package com.zerobase.Cafe_JOO.front.order.domain;

import com.zerobase.Cafe_JOO.common.BaseTimeEntity;
import com.zerobase.Cafe_JOO.common.type.OrderCookingStatus;
import com.zerobase.Cafe_JOO.common.type.OrderCookingTime;
import com.zerobase.Cafe_JOO.common.type.OrderReceiptStatus;
import com.zerobase.Cafe_JOO.common.type.Payment;
import com.zerobase.Cafe_JOO.front.member.domain.Member;
import com.zerobase.Cafe_JOO.front.order.dto.OrderAddDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "ORDERS")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Payment payment;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderCookingStatus cookingStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderCookingTime cookingTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderReceiptStatus receiptStatus;

    private LocalDateTime receivedTime;

    public void modifyReceivedTime(OrderCookingStatus newStatus) {
        if (this.cookingStatus == OrderCookingStatus.NONE
                && newStatus == OrderCookingStatus.COOKING) {
            this.receivedTime = LocalDateTime.now();
        }
        this.cookingStatus = newStatus;
    }

    public void modifyReceiptStatus(OrderReceiptStatus newReceiptStatus) {
        this.receiptStatus = newReceiptStatus;
    }

    public void modifyCookingTime(OrderCookingTime selectedCookingTime) {
        this.cookingTime = selectedCookingTime;
    }

    // builder 패턴을 사용한 객체 생성
    public static Order fromAddOrderDto(OrderAddDto.Request dto, Member member) {
        return Order.builder()
                .member(member)                             // 주문을 생성한 회원 정보 설정
                .payment(dto.getPayment())                  // DTO로부터 결제 정보 설정
                .cookingStatus(OrderCookingStatus.NONE)     // 초기 요리 상태는 NONE으로 설정
                .cookingTime(null)                          // 요리 시간이 아직 설정되지 않음
                .receiptStatus(OrderReceiptStatus.WAITING)  // 영수증 상태는 대기 중(WAITING)으로 설정
                .build();
    }
}