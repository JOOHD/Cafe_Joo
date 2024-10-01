package com.zerobase.Cafe_JOO.front.order.dto;

import com.zerobase.Cafe_JOO.common.type.OrderReceiptStatus;
import com.zerobase.Cafe_JOO.front.order.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderHisDto {

    public OrderHisDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getCreatedDate();
        this.orderReceiptStatus = order.getReceiptStatus();
        orderedProductsList = new ArrayList<>();
    }

    private Long orderId;

    private LocalDateTime orderDate;

    private OrderReceiptStatus orderReceiptStatus;

    private List<OrderProductDto> orderedProductsList;

    public void addOrderProductDto(OrderProductDto orderProductDto) {
        orderedProductsList.add(orderProductDto);
    }
}
