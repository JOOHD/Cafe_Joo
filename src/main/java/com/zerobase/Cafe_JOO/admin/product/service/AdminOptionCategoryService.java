package com.zerobase.Cafe_JOO.admin.product.service;

import org.springframework.stereotype.Service;

@Service
public interface AdminOptionCategoryService {

    // 관리자 옵션 카테고리 등록
    void addOptionCategory(AdminOptionCategoryForm.Request optionCategoryFormRequest);

    // 관리자 옵션 카테고리 수정
    void modifyOptionCategory(Integer optionCategoryId, AdminOptionCategoryForm.Request optionCategoryForm);

    void removeOptionCategory(Integer optionCategoryId);

    List<AdminOptionCategoryDto.Response> findOptionCategoryList();

    AdminOptionCategoryDto.Response findOptionCategoryListById(Integer optionCategoryId);
}
