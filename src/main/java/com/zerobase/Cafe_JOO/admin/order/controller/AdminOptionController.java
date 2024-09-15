package com.zerobase.Cafe_JOO.admin.order.controller;

import com.zerobase.Cafe_JOO.admin.product.dto.AdminOptionDto;
import com.zerobase.Cafe_JOO.admin.product.dto.AdminOptionForm;
import com.zerobase.Cafe_JOO.admin.product.service.AdminOptionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(name = "admin-option-controller", description = "관리자 옵션 CRUD API")
@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin/option")
@RequiredArgsConstructor
public class AdminOptionController {

    private final AdminOptionService adminOptionService;

    @ApiOperation(value = "옵션 등록", notes = "관리자가 옵션을 등록합니다.")
    @PostMapping
    public ResponseEntity<Void> optionAdd(
            @Valid @RequestBody AdminOptionForm.Request optionFormRequest) {
        adminOptionService.addOption(AdminOptionDto.Request.from(optionFormRequest));
        return ResponseEntity.status(CREATED).build();
    }

    @ApiOperation(value = "옵션 수정", notes = "관리자가 옵션의 정보를 수정합니다.")
    @PutMapping("/{optionId}")
    public ResponseEntity<Void> optionModify(
            @PathVariable Integer optionId,
            @Valid @RequestBody AdminOptionForm.Request form) {
        adminOptionService.modifyOption(optionId, AdminOptionDto.Request.from(form));
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @ApiOperation(value = "옵션 삭제", notes = "관리자가 옵션을 삭제합니다.")
    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> optionRemove(@PathVariable Integer optionId) {
        adminOptionService.removeOption(optionId);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @ApiOperation(value = "옵션 전체 조회", notes = "관리자가 전체 옵션 목록을 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<List<AdminOptionForm.Response>> optionList() {
        List<AdminOptionForm.Response> optionList = adminOptionService.findAllOption();
        return ResponseEntity.ok().body(optionList);
    }

    @ApiOperation(value = "옵션 단건 조회", notes = "관리자가 하나의 옵션에 대한 정보를 조회합니다.")
    @GetMapping("/{optionId}")
    public ResponseEntity<AdminOptionForm.Response> optionListById(@PathVariable Integer optionId) {
        AdminOptionForm.Response response = adminOptionService.findByIdOption(optionId);
        return ResponseEntity.ok().body(response);
    }
}
