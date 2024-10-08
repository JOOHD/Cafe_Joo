package com.zerobase.Cafe_JOO.front.product.controller;

import com.zerobase.Cafe_JOO.front.product.dto.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "product-controller", description = "상품 목록 및 상세조회 API")
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    @ApiOperation(value = "상품 카테고리 별 상품 목록 조회", notes = "상품 카테고리 별로 상품 목록을 조회합니다.")
    @GetMapping("/list/{productCategoryId}")
    public ResponseEntity<ProductListForm.Response> productList(@PathVariable Integer productCategoryId) {

        List<ProductDto> productDtoList = productService.findProductList(productCategoryId);

        ProductListForm.Response response =  ProductListForm.Response.builder()
                .productDtoList(productDtoList)
                .build();

        return ResponseEntity.status(OK).body(response);
    }

    @ApiOperation(value = "상품 상세 조회", notes = "상품의 상세 정보를 조회합니다.")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailForm.Response> productDetails(@PathVariable Integer productId) {
        ProductDetailDto productDetails = productService.findProductDetails(productId);

        return ResponseEntity.status(OK)
                .body(ProductDetailForm.Response.from(productDetails));
    }

    @ApiOperation(value = "베스트 상품 목록 조회", notes = "주문 수량을 기준으로 베스트 상품 5개를 조회합니다.")
    @GetMapping("/best-list")
    public ResponseEntity<BestProductForm> bestProductList() {
        List<BestProductDto> bestProduct = productService.findBestProductList();
        BestProductForm bestProductForm = BestProductForm.builder()
                .bestProducts(BestProductForm.BestProductResponse.from(bestProduct))
                .build();

        return ResponseEntity.ok(bestProductForm);
    }
}
