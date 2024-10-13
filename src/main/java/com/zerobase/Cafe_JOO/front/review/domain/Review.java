package com.zerobase.Cafe_JOO.front.review.domain;

import com.zerobase.Cafe_JOO.common.BaseTimeEntity;
import com.zerobase.Cafe_JOO.front.member.domain.Member;
import com.zerobase.Cafe_JOO.front.order.domain.OrderProduct;
import com.zerobase.Cafe_JOO.front.review.dto.ReviewAddDto.Request;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_product_id")
    private OrderProduct orderProduct;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
    //// ADD CONSTRAINT check_rating_range CHECK (rating >= 1 AND rating <= 5);

    private String content;

    private String picture;

    public static Review from(Member memberByToken, OrderProduct orderProduct,
                              Request request, String s3UploadedUrl) {
        return Review.builder()
                .memberId(memberByToken.getId())
                .orderProduct(orderProduct)
                .rating(request.getRating())
                .content(request.getContent())
                .picture(s3UploadedUrl)
                .build();
    }
}
