package com.zerobase.Cafe_JOO.admin.product.controller;

import com.zerobase.Cafe_JOO.admin.product.dto.AdminProductCategoryDto;
import com.zerobase.Cafe_JOO.admin.product.dto.AdminProductCategoryForm;
import com.zerobase.Cafe_JOO.admin.product.service.AdminProductCategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Tag(name = "admin-product-category-controller", description = "관리자 상품 카테고리 CRUD API")
@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin/category")
public class AdminProductCategoryController {

    private final AdminProductCategoryService adminProductCategoryService;

    @ApiOperation(value = "상품 카테고리 등록", notes = "관리자가 상품 카테고리 등록")
    @PostMapping
    public ResponseEntity<Void> productCategoryAdd(
            @Valid @RequestBody AdminProductCategoryForm.Request form) {
        adminProductCategoryService.addProductCategory(AdminProductCategoryDto.Request.from(form));
        return ResponseEntity.status(CREATED).build();
    }
}
