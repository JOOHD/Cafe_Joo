package com.zerobase.Cafe_JOO.front.order.dto;

import com.zerobase.Cafe_JOO.common.type.OrderReceiptStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderReceiptModifyForm {

    private OrderReceiptStatus newReceiptStatus;
}