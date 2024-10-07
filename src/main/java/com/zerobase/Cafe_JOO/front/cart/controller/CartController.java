package com.zerobase.Cafe_JOO.front.cart.controller;

import com.zerobase.Cafe_JOO.front.cart.dto.CartAddForm;
import com.zerobase.Cafe_JOO.front.cart.dto.CartListDto;
import com.zerobase.Cafe_JOO.front.cart.dto.CartListForm;
import com.zerobase.Cafe_JOO.front.cart.dto.CartListForm.Response;
import com.zerobase.Cafe_JOO.front.cart.dto.CartProductDto;
import com.zerobase.Cafe_JOO.front.cart.service.CartService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

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

        Response response = CartListForm.Response.builder()
                .cartListDtoList(cartListDtos)
                .build();

        return ResponseEntity.status(OK).body(response);
    }

    @ApiOperation(value = "상품 삭제", notes = "사용자가 상품을 삭제할 수 있습니다.")
    @DeleteMapping
    public ResponseEntity<List<CartProductDto>> cartRemove(
            @RequestBody @Valid CartAddForm cartAddForm,
            @RequestHeader(name = "Authorization") String token
    ) {
        return ResponseEntity.ok(cartService.removeCart(token, cartAddForm));
    }

    @ApiOperation(value = "장바구니 상품 넣기", notes = "사용자가 장바구니에 상품을 넣을 수 있다.")
    @PostMapping("/save")
    public ResponseEntity<List<CartProductDto>> cartSave(
            @RequestBody @Valid CartAddForm cartAddForm,
            @RequestHeader(name = "Authorization") String token
    ) {
        return ResponseEntity.ok(cartService.saveCart(token, cartAddForm));
    }
}
