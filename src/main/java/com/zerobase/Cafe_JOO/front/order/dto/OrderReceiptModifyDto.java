package com.zerobase.Cafe_JOO.front.order.dto;

import com.zerobase.Cafe_JOO.common.type.OrderReceiptStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderReceiptModifyDto {

    private OrderReceiptStatus newReceiptStatus;

    public static OrderReceiptModifyDto from(OrderReceiptModifyForm orderReceiptModifyForm) {
        return OrderReceiptModifyDto.builder()
                .newReceiptStatus(orderReceiptModifyForm.getNewReceiptStatus())
                .build();
    }
}
