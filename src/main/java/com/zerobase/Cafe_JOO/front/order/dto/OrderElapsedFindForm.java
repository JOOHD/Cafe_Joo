package com.zerobase.Cafe_JOO.front.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderElapsedFindForm {

    private Long elapsedTimeMinutes;

    /*
    // 생성자
    public OrderElapsedFindForm(Long elapsedTimeMinutes) {
        this.elapsedTimeMinutes = elapsedTimeMinutes;
    }

    // Getter
    public Long getElapsedTimeMinutes() {
        return elapsedTimeMinutes;
    }
    */

}
