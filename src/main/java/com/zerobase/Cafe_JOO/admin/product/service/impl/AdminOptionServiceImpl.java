package com.zerobase.Cafe_JOO.admin.product.service.impl;


import com.zerobase.Cafe_JOO.admin.product.dto.AdminOptionDto;
import com.zerobase.Cafe_JOO.admin.product.dto.AdminOptionForm;
import com.zerobase.Cafe_JOO.admin.product.service.AdminOptionService;
import com.zerobase.Cafe_JOO.common.exception.CustomException;
import com.zerobase.Cafe_JOO.front.product.domain.Option;
import com.zerobase.Cafe_JOO.front.product.domain.OptionCategory;
import com.zerobase.Cafe_JOO.front.product.domain.OptionCategoryRepository;
import com.zerobase.Cafe_JOO.front.product.domain.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.zerobase.Cafe_JOO.common.exception.ErrorCode.OPTION_CATEGORY_NOT_EXISTS;
import static com.zerobase.Cafe_JOO. common.exception.ErrorCode.OPTION_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class AdminOptionServiceImpl implements AdminOptionService {

    private final OptionRepository optionRepository;
    private final OptionCategoryRepository optionCategoryRepository;

    // 옵션 등록
    @Override
    public void addOption(AdminOptionDto.Request optionAddDto) {
        Integer optionCategoryId = optionAddDto.getOptionCategoryId();
        OptionCategory optionCategory = optionCategoryRepository.findById(optionCategoryId)
                .orElseThrow(() -> new CustomException(OPTION_CATEGORY_NOT_EXISTS));
        Option option = Option.builder()
                .optionCategory(optionCategory)
                .name(optionAddDto.getName())
                .price(optionAddDto.getPrice())
                .build();
        optionRepository.save(option);
    }

    // 옵션 수정
    @Override
    public void modifyOption(Integer id, AdminOptionDto.Request request) {
        Integer optionCategoryId = request.getOptionCategoryId();
        OptionCategory optionCategory = optionCategoryRepository.findById(optionCategoryId)
                .orElseThrow(() -> new CustomException(OPTION_CATEGORY_NOT_EXISTS));
        Option optionId = optionRepository.findById(id)
                .orElseThrow(() -> new CustomException(OPTION_NOT_EXISTS));

        Option modifyOption = Option.builder()
                .optionCategory(optionCategory)
                .name(request.getName())
                .price(request.getPrice())
                .build();

        optionRepository.save(modifyOption.toBuilder()
                .id(optionId.getId())
                .build());
    }

    // 옵션 삭제
    @Override
    public void removeOption(Integer id) {
        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new CustomException(OPTION_NOT_EXISTS));
        optionRepository.deleteById(option.getId());
    }

    // 옵션 전체 조회
    @Override
    public List<AdminOptionForm.Response> findAllOption() {
        List<Option> optionList = optionRepository.findAll();
        List<AdminOptionForm.Response> optionDtoList = optionList.stream()
                .map(AdminOptionForm.Response::from)
                .collect(Collectors.toList());
        return optionDtoList;
    }

    // 옵션Id별 조회
    @Override
    public AdminOptionForm.Response findByIdOption(Integer id) {
        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new CustomException(OPTION_NOT_EXISTS));
        AdminOptionForm.Response response = AdminOptionForm.Response.from(option);
        return response;
    }
}


