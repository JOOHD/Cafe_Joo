package com.zerobase.Cafe_JOO.front.order.dto;

import com.zerobase.Cafe_JOO.common.type.OrderCookingTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCookingTimeModifyDto {

    private OrderCookingTime selectedCookingTime;

    public static OrderCookingTimeModifyDto from(OrderCookingTimeModifyForm form) {
        return OrderCookingTimeModifyDto.builder()
                .selectedCookingTime(form.getSelectedCookingTime())
                .build();
    }
}
