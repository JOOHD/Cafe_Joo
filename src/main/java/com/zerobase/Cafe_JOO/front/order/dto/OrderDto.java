package com.zerobase.Cafe_JOO.front.order.dto;

import com.zerobase.Cafe_JOO.common.type.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OrderAddDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private Payment payment;

        public static OrderAddDto.Request from(OrderAddForm.Request form) {
            return Request.builder()
                    .payment(form.getPayment())
                    .build();
        }
    }
}