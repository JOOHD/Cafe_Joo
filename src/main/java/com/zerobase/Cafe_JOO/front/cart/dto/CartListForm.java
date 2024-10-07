package com.zerobase.Cafe_JOO.front.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class CartListForm {

    @AllArgsConstructor
    @Builder
    @Getter
    public static class Response {

        private List<CartListDto> cartListDtoList;

    }
}
