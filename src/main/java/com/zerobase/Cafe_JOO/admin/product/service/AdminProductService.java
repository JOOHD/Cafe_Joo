package com.zerobase.Cafe_JOO.admin.product.service;

import com.zerobase.Cafe_JOO.admin.product.dto.AdminProductDto;
import com.zerobase.Cafe_JOO.admin.product.dto.AdminProductForm;
import com.zerobase.Cafe_JOO.common.type.SoldOutStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface AdminProductService {

    // 상품 추가
    void addProduct(MultipartFile image, AdminProductDto adminProductDto) throws IOException;

    // 상품 수정
    void modifyProduct(MultipartFile image, Integer id, AdminProductDto adminProductDto)
            throws IOException;

    // 상품 삭제
    void removeProduct(Integer id) throws IOException;

    // 상품 전체 조회
    List<AdminProductForm.Response> findProductList();

    // 상품Id별 조회
    AdminProductForm.Response findProductById(Integer id);

    // 상품 품절여부 수정
    void modifyProductSoldOut(Integer id, SoldOutStatus soldOutStatus);
}
