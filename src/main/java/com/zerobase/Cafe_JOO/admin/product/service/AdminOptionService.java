package com.zerobase.Cafe_JOO.admin.product.service;


import com.zerobase.Cafe_JOO.admin.product.dto.AdminOptionDto;
import com.zerobase.Cafe_JOO.admin.product.dto.AdminOptionForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminOptionService {

    // 옵션 등록
    void addOption(AdminOptionDto.Request optionAddDto);

    // 옵션 수정
    void modifyOption(Integer id, AdminOptionDto.Request request);

    // 옵션 삭제
    void removeOption(Integer id);

    // 옵션 전체 조회
    List<AdminOptionForm.Response> findAllOption();

    // 옵션Id별 조회
    AdminOptionForm.Response findByIdOption(Integer id);
}
