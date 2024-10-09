package com.zerobase.Cafe_JOO.front.product.dto;

import com.zerobase.Cafe_JOO.common.type.SoldOutStatus;
import com.zerobase.Cafe_JOO.front.product.domain.Option;
import com.zerobase.Cafe_JOO.front.product.domain.Product;
import com.zerobase.Cafe_JOO.front.product.domain.ProductOptionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Builder
@Getter
public class ProductDetailDto {

    private Integer productId;

    private String name;

    private String description;

    private Integer price;

    private SoldOutStatus soldOutStatus;

    private String picture;

    private Map<ProductOptionCategory, List<Option>> productOptionList;


    public static ProductDetailDto from(Product product,
                                        Map<ProductOptionCategory, List<Option>> productOptionList) {
        return ProductDetailDto.builder()
                .productId(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .soldOutStatus(product.getSoldOutStatus())
                .picture(product.getPicture())
                .productOptionList(productOptionList)
                .build();
    }
}
