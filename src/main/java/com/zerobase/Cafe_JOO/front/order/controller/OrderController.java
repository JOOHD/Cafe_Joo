package com.zerobase.Cafe_JOO.front.order.controller;

import com.zerobase.Cafe_JOO.common.config.security.TokenProvider;
import com.zerobase.Cafe_JOO.common.exception.CustomException;
import com.zerobase.Cafe_JOO.common.exception.ErrorCode;
import com.zerobase.Cafe_JOO.front.order.dto.OrderAddDto;
import com.zerobase.Cafe_JOO.front.order.dto.OrderAddForm;
import com.zerobase.Cafe_JOO.front.order.dto.OrderElapsedFindForm;
import com.zerobase.Cafe_JOO.front.order.dto.OrderHisDto;
import com.zerobase.Cafe_JOO.front.order.service.impl.OrderHistoryService;
import com.zerobase.Cafe_JOO.front.order.service.impl.OrderService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(name = "order-controller", description = "사용자 주문 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/order")
@PreAuthorize("hasRole('ROLE_USER')")
public class OrderController {

    private final OrderHistoryService orderHistoryService;
    private final OrderService orderService;
    private final TokenProvider tokenProvider;

    @ApiOperation(value =  "조리 경과 시간 조회", notes = "사용자가 조리 경과 시간을 조회합니다.")
    @GetMapping("/elapsed-time/{orderId}")
    public ResponseEntity<OrderElapsedFindForm> elapsedTimeGet(
        // public static final String TOKEN_HEADER = "Authorization"; // 상수 설정
        // String token = request.getHeader(TOKEN_HEADER); // 요청 헤더에서 'Authorization' 값을 가져옴.
        @RequestHeader(name = "Authorization") String token,
        @PathVariable Long orderId) {
        Long elapsedTimeMinutes = orderService.findElapsedTime(token, orderId);

        // @Builder - X, setter 사용
        // OrderElapsedFindForm response = new OrderElapsedFindForm();
        // response.setElapsedTimeMinutes(elapsedTimeMinutes);
        OrderElapsedFindForm response = OrderElapsedFindForm.builder()
                .elapsedTimeMinutes(elapsedTimeMinutes)
                .build();
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "주문 취소", notes = "관리자가 주문을 수락하기 전에 사용자가 주문을 취소합니다.")
    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<Void> orderCancelModify(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable Long orderId) {
        orderService.modifyOrderCancel(token, orderId);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @ApiOperation(value = "주문 내역 저장",
        notes = "사용자의 토큰을 받아 현재 장바구니에 담겨있는 목록들을 주문 내역 테이블에 저장합니다.")
    @PostMapping
    public ResponseEntity<Void> orderAdd(@RequestHeader(name = "Authorization") String token,
        @Valid @RequestBody OrderAddForm.Request orderAddForm) {
        orderService.addOrder(token, OrderAddDto.Request.from(orderAddForm));
        return ResponseEntity.status(CREATED).build();
    }

    @ApiOperation(value = "주문 내역 조회",
        notes = "기본적으로 3개월간의 내역이 조회되고, 기간 별 필터링을 해서 조회할 수 있습니다.")
    @GetMapping("/list")
    public ResponseEntity<List<OrderHisDto>> getOrderHistoryList(
        @RequestHeader(name = "Authorization") String token,
        @RequestParam(value = "viewType", required = false) String viewType,
        @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        Long memberId = tokenProvider.getId(token);

        if ("기간".equals(viewType) && (startDate == null || endDate == null)) {
            throw new CustomException(ErrorCode.START_DATE_AND_END_DATE_ARE_ESSENTIAL);
        }

        List<OrderHisDto> orderHisDtoList;

        if ("전체".equals(viewType)) {
            orderHisDtoList = orderHistoryService.findAllOrderHistory(memberId);
        } else if ("기간".equals(viewType) && startDate != null && endDate != null) {
            orderHisDtoList = orderHistoryService.findOrderHistoryByPeriod(memberId, startDate, endDate);
        } else {
            orderHisDtoList = orderHistoryService.findOrderHistoryFor3Months(memberId);
        }

        return ResponseEntity.ok(orderHisDtoList);
    }
}




















