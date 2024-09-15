package com.zerobase.Cafe_JOO.admin.product.service;

import com.zerobase.Cafe_JOO.admin.product.dto.AdminOptionCategoryDto;
import com.zerobase.Cafe_JOO.admin.product.dto.AdminOptionCategoryForm;
import org.springframework.stereotype.Service;

import java.util.List;

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
