package com.zerobase.Cafe_JOO.front.product.dto;

import com.zerobase.Cafe_JOO.common.type.SoldOutStatus;
import com.zerobase.Cafe_JOO.front.product.domain.Option;
import com.zerobase.Cafe_JOO.front.product.domain.ProductOptionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public class ProductDetailForm {

    @AllArgsConstructor
    @Builder
    @Getter
    public static class Response {

        private Integer productId;

        private String name;

        private String description;

        private Integer price;

        private SoldOutStatus soldOutStatus;

        private String picture;

        private Map<ProductOptionCategory, List<Option>> productOptionList;

        public static ProductDetailForm.Response from(
                ProductDetailDto productDetailDto) {
            return ProductDetailForm.Response.builder()
                    .productId(productDetailDto.getProductId())
                    .name(productDetailDto.getName())
                    .description(productDetailDto.getDescription())
                    .price(productDetailDto.getPrice())
                    .soldOutStatus(productDetailDto.getSoldOutStatus())
                    .picture(productDetailDto.getPicture())
                    .productOptionList(productDetailDto.getProductOptionList())
                    .build();
        }
    }
}
