package com.zerobase.Cafe_JOO.admin.product.service.impl;

import com.zerobase.Cafe_JOO.admin.product.dto.AdminProductDto;
import com.zerobase.Cafe_JOO.admin.product.service.AdminProductService;
import com.zerobase.Cafe_JOO.common.S3UploaderService;
import com.zerobase.Cafe_JOO.common.exception.CustomException;
import com.zerobase.Cafe_JOO.front.product.domain.Product;
import com.zerobase.Cafe_JOO.front.product.domain.ProductCategory;
import com.zerobase.Cafe_JOO.front.product.domain.ProductCategoryRepository;
import com.zerobase.Cafe_JOO.front.product.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


import static com.zerobase.Cafe_JOO.common.exception.ErrorCode.PRODUCTCATEGORY_NOT_EXISTS;
import static com.zerobase.Cafe_JOO.common.exception.ErrorCode.PRODUCT_NOT_EXISTS;
@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final S3UploaderService s3UploaderService;

    // 관리자 상품 등록
    @Override
    public void addProduct(MultipartFile image, AdminProductDto adminProductDto)
            throws IOException {
        String pictureUrl = s3UploaderService.uploadFileToS3(image, "product");

        Integer productCategoryId = adminProductDto.getProductCategoryId();
        ProductCategory productCategory = productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new CustomException(PRODUCTCATEGORY_NOT_EXISTS));

        productRepository.save(Product.builder()
                .name(adminProductDto.getName())
                .description(adminProductDto.getDescription())
                .productCategory(productCategory)
                .price(adminProductDto.getPrice())
                .soldOutStatus(adminProductDto.getSoldOutStatus())
                .picture(pictureUrl)
                .build());
    }

    @Override

}
