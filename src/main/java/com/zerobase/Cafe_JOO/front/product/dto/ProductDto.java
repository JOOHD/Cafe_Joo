package com.zerobase.Cafe_JOO.front.product.dto;

import com.zerobase.Cafe_JOO.common.type.SoldOutStatus;
import com.zerobase.Cafe_JOO.front.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ProductDto {

    private Integer productId;

    private String name;

    private Integer price;

    private SoldOutStatus soldOutStatus;

    private String picture;

    public static ProductDto from(Product product) {
        return ProductDto.builder()
                .productId(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .soldOutStatus(product.getSoldOutStatus())
                .picture(product.getPicture())
                .build();
    }
}
