package com.zerobase.Cafe_JOO.front.cart.dto;

import com.zerobase.Cafe_JOO.common.type.CartOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartAddForm {

    @NotNull(message = "productId는 필수입니다.")
    private Integer productId;

    @NotNull(message = "quantity는 필수입니다.")
    private Integer quantity;

    private List<Integer> optionIdList;

    @NotNull(message = "cartOrderStatus는 필수입니다.")
    private CartOrderStatus cartOrderStatus;
}
