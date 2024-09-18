package com.zerobase.Cafe_JOO.admin.product.service.impl;

import com.zerobase.Cafe_JOO.admin.product.dto.AdminProductCategoryDto;
import com.zerobase.Cafe_JOO.admin.product.service.AdminProductCategoryService;
import com.zerobase.Cafe_JOO.common.exception.CustomException;
import com.zerobase.Cafe_JOO.common.exception.ErrorCode;
import com.zerobase.Cafe_JOO.front.product.domain.ProductCategory;
import com.zerobase.Cafe_JOO.front.product.domain.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.zerobase.Cafe_JOO.common.exception.ErrorCode.PRODUCTCATEGORY_NOT_EXISTS;
import static com.zerobase.Cafe_JOO.common.exception.ErrorCode.PRODUCT_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class AdminProductCategoryServiceImpl implements AdminProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    // 관리자 상품 등록
    @Override
    public void addProductCategory(AdminProductCategoryDto.Request productCategoryDto) {

        productCategoryRepository.findByName(productCategoryDto.getName())
                        .ifPresent(name->{
                            throw new CustomException((ErrorCode.PRODUCTCATEGORY_ARLEADY_EXISTS));
                        });

        productCategoryRepository.save(
                ProductCategory.builder()
                        .name(productCategoryDto.getName())
                        .build());
    }

    // 상품 카테고리 수정
    @Transactional
    @Override
    public void modifyProductCategory(Integer productCategoryId, AdminProductCategoryDto.Request productCategoryDto) {

        productCategoryRepository.findByName(productCategoryDto.getName())
                .ifPresent(name->{
                    throw new CustomException(ErrorCode.PRODUCTCATEGORY_ARLEADY_EXISTS);
                });
        ProductCategory productCategory = productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new CustomException(PRODUCTCATEGORY_NOT_EXISTS));
        productCategory.modifyProductCategory(productCategoryDto.getName());
    }
    
    // 상품 카테고리 삭제
    @Override
    public void removeProductCategory(Integer productCategoryId) {
        ProductCategory productCategory = productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_EXISTS));
        productCategoryRepository.deleteById(productCategory.getId());
    }

    // 상품 카테고리 전체 조회
    @Override
    public List<AdminProductCategoryDto.Response> findAllProductCategory() {
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        List<AdminProductCategoryDto.Response> productCategoryDtoList = productCategoryList.stream()
                .map(AdminProductCategoryDto.Response::from)
                .collect(Collectors.toList());
        return productCategoryDtoList;
    }
}






















