package com.zerobase.Cafe_JOO.front.order.dto;

import com.zerobase.Cafe_JOO.front.order.domain.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderProductDto {

    public OrderProductDto(OrderProduct orderProduct) {
        this.productName = orderProduct.getProduct().getName();
        this.price = orderProduct.getProduct().getPrice();
    }

    private String productName;

    private Integer price;
}

