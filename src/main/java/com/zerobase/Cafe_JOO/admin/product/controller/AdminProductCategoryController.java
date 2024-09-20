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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

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

    @ApiOperation(value = "상품 카테고리 수정", notes = "관리자가 상품 카테고리 이름을 수정합니다.")
    @PutMapping("/{productCategoryId}")
    public ResponseEntity<Void> productCategoryModify(
            @PathVariable Integer productCategoryId,
            @Valid @RequestBody AdminProductCategoryForm.Request form) {
        adminProductCategoryService.modifyProductCategory(productCategoryId,
                AdminProductCategoryDto.Request.from(form));
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @ApiOperation(value = "상품 카테고리 삭제", notes = "관리자가 상품 카테고리를 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<Void> productCategoryRemove(@PathVariable Integer productCategoryId) {
        adminProductCategoryService.removeProductCategory(productCategoryId);
        return  ResponseEntity.status(NO_CONTENT).build();
    }

    @ApiOperation(value = "상품 카테고리 전체 조회", notes = "관리자가 상품 카테고리를 전체조회합니다.")
    @GetMapping
    public ResponseEntity<List<AdminProductCategoryForm.Response>> productCategoryList1() {
        List<AdminProductCategoryDto.Response> productCategoryList = adminProductCategoryService.findAllProductCategory();
        List<AdminProductCategoryForm.Response> productCategoryFormList = productCategoryList.stream()
                .map(AdminProductCategoryForm.Response::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(productCategoryFormList);
    }
}
