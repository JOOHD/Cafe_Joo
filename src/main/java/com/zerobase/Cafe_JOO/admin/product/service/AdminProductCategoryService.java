package com.zerobase.Cafe_JOO.admin.product.service;

import com.zerobase.Cafe_JOO.admin.product.dto.AdminProductCategoryDto;

import java.util.List;

public interface AdminProductCategoryService {

    // 관리자 상품 카테고리 등록
    void addProductCategory(AdminProductCategoryDto.Request productCategoryDto);

    // 상품 카테고리 수정
    void modifyProductCategory(Integer productCategoryId, AdminProductCategoryDto.Request productCategoryDto);

    // 상품 카테고리 삭제
    void removeProductCategory(Integer productCategoryId);

    // 상품 카테고리 전체 조회
    List<AdminProductCategoryDto.Response> findAllProductCategory();

    // 상품 카테고리Id 별 조회
    AdminProductCategoryDto.Response findByIdProductCategory(Integer productCategoryId);
}
