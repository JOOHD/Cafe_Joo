package com.zerobase.Cafe_JOO.admin.order.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "admin-order-controller", description = "관리자 주문 관련 API")
@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/order")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @ApiOperation(value = "주문 상태 변경", notes = "관리자가 주문 상태를 변경합니다.")
    @PatchMapping("/status/{orderId}")
    public ResponseEntity<Void> orderStatusModify(
        @PathVariable Long orderId,
        @RequestBody @Valid OrderStatusModifyForm orderStatusModifyForm) {

        adminOrderService.modifyOrderStatus(orderId,
                OrderStatusModifyDto.from(orderStatusModifyForm));
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @ApiOperation(value = "주문 수락 또는 거절", notes = "관리자가 주문을 수락 또는 거절합니다.")
    @PatchMapping("/receipt-status/{orderId}")
    public ResponseEntity<Void> orderReceiptModify(
        @PathVariable Long orderId,
        @RequestBody @Valid OrderReceiptModifyForm orderReceiptModifyForm) {

        adminOrderService.modifyOrderReceiptStatus(orderId,
                OrderReceiptModifyDto.from(orderReceiptModifyForm));
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @ApiOperation(value = "주문 조리 예정 시간 선택", notes = "관리자가 수락된 주문 조리 예정 시간을 선택합니다.")
    @PatchMapping("/cooking-time/{orderId}")
    public ResponseEntity<Void> orderCookingTimeModify(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderCookingTimeModifyForm cookingTimeModifyForm) {

        adminOrderService.modifyOrderCookingTime(orderId,
                OrderCookingTimeModifyDto.from(cookingTimeModifyForm));
        return ResponseEntity.status(NO_CONTENT).build();
    }
}


