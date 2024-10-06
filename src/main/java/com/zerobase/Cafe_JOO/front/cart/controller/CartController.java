package com.zerobase.Cafe_JOO.front.cart.controller;


import com.zerobase.Cafe_JOO.front.cart.dto.CartListDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "cart-controller", description = "장바구니 관련 API")
@RestController
@RequestMapping("/auth/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @ApiOperation(value = "장바구니 목록 조회", notes = "사용자가 장바구니 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<Response> cartList(
        @RequestHeader(name = "Authorization") String token) {

        List<CartListDto> cartListDtos = cartService.findCartList(token);
    }
}
