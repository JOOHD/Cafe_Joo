package com.zerobase.Cafe_JOO.front.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class ProductListForm {

    @AllArgsConstructor
    @Builder
    @Getter
    public static class Response {

        private List<ProductDto> productDtoList;
    }
}