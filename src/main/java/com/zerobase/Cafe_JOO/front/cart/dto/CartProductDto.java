package com.zerobase.Cafe_JOO.front.cart.dto;

import com.zerobase.Cafe_JOO.front.cart.domain.Cart;
import com.zerobase.Cafe_JOO.front.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class CartProductDto {

    private Product product;

    private Integer quantity;

    private List<Integer> optionIdList;

    public void addOptionId(Integer optionId) {
        optionIdList.add(optionId);
    }

    public static CartProductDto from(Cart cart) {
        return CartProductDto.builder()
                .product(cart.getProduct())
                .quantity(cart.getQuantity())
                .optionIdList(new ArrayList<>())
                .build();
    }
}
