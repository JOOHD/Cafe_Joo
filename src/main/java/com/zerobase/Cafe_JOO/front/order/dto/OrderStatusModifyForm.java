package com.zerobase.Cafe_JOO.front.order.dto;

import com.zerobase.Cafe_JOO.common.type.OrderCookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusModifyForm { // 요청 데이터 수집(form data binding==)

    private OrderCookingStatus newStatus;
}