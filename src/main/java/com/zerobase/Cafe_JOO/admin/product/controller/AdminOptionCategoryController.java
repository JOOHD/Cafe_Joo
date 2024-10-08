package com.zerobase.Cafe_JOO.admin.product.controller;

import com.zerobase.Cafe_JOO.admin.product.dto.AdminOptionCategoryDto;
import com.zerobase.Cafe_JOO.admin.product.dto.AdminOptionCategoryForm;
import com.zerobase.Cafe_JOO.admin.product.service.AdminOptionCategoryService;
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

@Tag(name = "admin-option-category-controller", description = "관리자 옵션 카테고리 CRUD API")
@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin/option-category")
public class AdminOptionCategoryController {

    private final AdminOptionCategoryService adminOptionCategoryService;

    @ApiOperation(value = "옵션 카테고리 등록", notes ="관리자가 옵션 카테고리를 등록합니다.")
    @PostMapping
    public ResponseEntity<Void> OptionCategoryAdd(
            @Valid @RequestBody AdminOptionCategoryForm.Request optionCategoryFormRequest) {
        adminOptionCategoryService.addOptionCategory(optionCategoryFormRequest);
        return ResponseEntity.status(CREATED).build();
    }

    @ApiOperation(value = "옵션 카테고리 삭제", notes = "관리자가 옵션 카테고리를 삭제합니다.")
    @DeleteMapping("/{optionCategoryId}")
    public ResponseEntity<Void> OptionCategoryRemove(@PathVariable Integer optionCategoryId) {
        adminOptionCategoryService.removeOptionCategory(optionCategoryId);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @ApiOperation(value = "옵션 카테고리 전체 목록 조회", notes = "관리자가 옵션 카테고리 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<AdminOptionCategoryForm.Response>> OptionCategoryList() {
        List<AdminOptionCategoryDto.Response> optionCategoryDtoList =
                adminOptionCategoryService.findOptionCategoryList();
        List<AdminOptionCategoryForm.Response> adminOptionCategoryFormList =
                optionCategoryDtoList.stream() // DTO 리스트 stream()으로 변환
                        .map(AdminOptionCategoryForm.Response::from) // 각 DTO를 Form으로 변환
                        .collect(Collectors.toList());               // 변환된 결과를 리스트로 수집
        return ResponseEntity.ok(adminOptionCategoryFormList);
    }

    @ApiOperation(value = "옵션 카테고리 단건 조회", notes = "관리자가 하나의 옵션 카테고리에 대한 정보를 조회합니다.")
    @GetMapping("/{optionCategoryId}")
    public ResponseEntity<AdminOptionCategoryForm.Response> optionCategoryListById(
            @PathVariable Integer optionCategoryId) {
        AdminOptionCategoryDto.Response optionCategoryDto =
                adminOptionCategoryService.findOptionCategoryListById(optionCategoryId);
        AdminOptionCategoryForm.Response optionCategoryForm =
                AdminOptionCategoryForm.Response.from(optionCategoryDto);
        return ResponseEntity.ok(optionCategoryForm);
    }
}
