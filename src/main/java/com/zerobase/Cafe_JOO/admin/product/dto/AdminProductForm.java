package com.zerobase.Cafe_JOO.admin.product.dto;

import com.zerobase.Cafe_JOO.common.type.SoldOutStatus;
import com.zerobase.Cafe_JOO.front.product.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
public class AdminProductForm {

    @NotNull
    private Integer productCategoryId;

    @Schema(description = "상품명", example = "아메리카노")
    @Size(min = 1, max = 10, message = "상품명은 1~10 자리로 입력해야 합니다.")
    @NotNull(message = "상품명은 필수로 입력해야 합니다.")
    private String name;

    @Schema(description = "상품 설명", example = "대표 음료 입니다.")
    @NotNull(message = "상품 설명은 필수로 입력해야 합니다.")
    private String description;

    @Schema(description = "상품 가격", example = "2800")
    @NotNull(message = "상품 가격은 필수로 입력해야 합니다.")
    private Integer price;

    @Schema(description = "품절여부", example = "SOLD_OUT")
    @NotNull(message = "품절여부는 필수로 입력해야 합니다.")
    private SoldOutStatus soldOutStatus;

    private String picture;

    @Getter
    @Builder
    public static class Response {

        private Integer id;

        private Integer productCategoryId;

        private String name;

        private String description;

        private Integer price;

        private SoldOutStatus soldOutStatus;

        private String picture;

        public static Response from(Product product) {
            return Response.builder()
                    .id(product.getId())
                    .productCategoryId(product.getProductCategory().getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .soldOutStatus(product.getSoldOutStatus())
                    .picture(product.getPicture())
                    .build();
        }
    }

}
