package com.zerobase.Cafe_JOO.admin.product.service.impl;

import com.zerobase.Cafe_JOO.admin.product.dto.AdminOptionCategoryDto;
import com.zerobase.Cafe_JOO.admin.product.dto.AdminOptionCategoryForm;
import com.zerobase.Cafe_JOO.admin.product.service.AdminOptionCategoryService;
import com.zerobase.Cafe_JOO.common.exception.CustomException;
import com.zerobase.Cafe_JOO.common.exception.ErrorCode;
import com.zerobase.Cafe_JOO.front.product.domain.OptionCategory;
import com.zerobase.Cafe_JOO.front.product.domain.OptionCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.zerobase.Cafe_JOO.common.exception.ErrorCode.OPTION_CATEGORY_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class AdminOptionCategoryServiceImpl implements AdminOptionCategoryService {

    private final OptionCategoryRepository optionCategoryRepository;

    // 관리자 옵션 카테고리 등록
    @Override
    public void addOptionCategory(AdminOptionCategoryForm .Request optionCategoryFormRequest) {
        AdminOptionCategoryDto.Request optionCategoryDto = AdminOptionCategoryDto.Request.from(
                optionCategoryFormRequest);

        optionCategoryRepository.save(OptionCategory.builder()
                .name(optionCategoryDto.getName())
                .build());
    }

    // 관리자 옵션 카테고리 수정
    @Transactional
    @Override
    public void modifyOptionCategory(Integer optionCategoryId,
         AdminOptionCategoryForm.Request optionCategoryForm) {
         OptionCategory optionCategory = optionCategoryRepository.findById(optionCategoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.OPTION_CATEGORY_NOT_EXISTS));

         AdminOptionCategoryDto.Request optionCategoryDto = AdminOptionCategoryDto.Request.from(
                 optionCategoryForm);

         optionCategory.modifyOptionCategoryDto(optionCategoryDto.getName());

         optionCategoryRepository.save(optionCategory);
    }

    // 관리자 옵션 카테고리 삭제
    @Override
    public void removeOptionCategory(Integer optionCategoryId) {
        OptionCategory optionCategory = optionCategoryRepository.findById(optionCategoryId)
                .orElseThrow(() -> new CustomException(OPTION_CATEGORY_NOT_EXISTS));
        optionCategoryRepository.deleteById(optionCategoryId);
    }
    
    // 관리자 옵션 카테고리 전제 조회
    @Override
    public List<AdminOptionCategoryDto.Response> findOptionCategoryList() {
        List<OptionCategory> optionCategoryList = optionCategoryRepository.findAll();
        List<AdminOptionCategoryDto.Response> optionCategoryDtoList =
                optionCategoryList.stream()
                        .map(AdminOptionCategoryDto.Response::from)
                        .collect(Collectors.toList());
        return optionCategoryDtoList;
    }

    // 관리자 옵션 카테고리 id별 조회
    @Override
    public AdminOptionCategoryDto.Response findOptionCategoryListById(Integer optionCategoryId) {
        OptionCategory optionCategory = optionCategoryRepository.findById(optionCategoryId)
                .orElseThrow(() -> new CustomException(OPTION_CATEGORY_NOT_EXISTS));
        AdminOptionCategoryDto.Response optionCategoryDto = AdminOptionCategoryDto.Response.from(
                optionCategory);
        return optionCategoryDto;
    }
}
