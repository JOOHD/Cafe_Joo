package com.zerobase.Cafe_JOO.front.product.dto;

import com.zerobase.Cafe_JOO.common.type.SoldOutStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class BestProductForm {

    private List<BestProductResponse> bestProducts;

    @Getter
    @Builder
    public static class BestProductResponse {

        private Integer productId;

        private String name;

        private Integer price;

        private SoldOutStatus soldOutStatus;

        private String picture;

        public static List<BestProductForm.BestProductResponse> from(
            List<BestProductDto> bestProductDto) {
            return bestProductDto.stream()
                .map(productDto -> BestProductForm.BestProductResponse.builder()
                        .productId(productDto.getProductId())
                        .name(productDto.getName())
                        .price(productDto.getPrice())
                        .soldOutStatus(productDto.getSoldOutStatus())
                        .picture(productDto.getPicture())
                        .build())
                .collect(Collectors.toList());
        }
    }
}
