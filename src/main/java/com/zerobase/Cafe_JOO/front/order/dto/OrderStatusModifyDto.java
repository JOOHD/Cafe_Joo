package com.zerobase.Cafe_JOO.front.order.dto;

import com.zerobase.Cafe_JOO.common.type.OrderCookingStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderStatusModifyDto {

    /*
        from 메서드는 정적 팩토리 메서드로,
        OrderStatusModifyForm 객체를 받아,
        OrderStatusModifyDto 객체를 생성합니다.
    */

    private OrderCookingStatus newStatus;

    public static OrderStatusModifyDto from(OrderStatusModifyForm orderStatusModifyForm) {
        return OrderStatusModifyDto.builder()
                .newStatus(orderStatusModifyForm.getNewStatus())
                .build();
    }
}

