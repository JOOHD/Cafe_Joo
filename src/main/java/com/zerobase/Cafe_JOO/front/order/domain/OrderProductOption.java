package com.zerobase.Cafe_JOO.front.order.domain;

import com.zerobase.Cafe_JOO.common.BaseTimeEntity;
import com.zerobase.Cafe_JOO.front.product.domain.Option;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderProductOption extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long orderProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option option;
}
