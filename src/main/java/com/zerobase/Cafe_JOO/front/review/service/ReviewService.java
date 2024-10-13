package com.zerobase.Cafe_JOO.front.review.service;

import com.zerobase.Cafe_JOO.common.S3UploaderService;
import com.zerobase.Cafe_JOO.common.config.security.TokenProvider;
import com.zerobase.Cafe_JOO.common.exception.CustomException;
import com.zerobase.Cafe_JOO.front.member.domain.Member;
import com.zerobase.Cafe_JOO.front.member.domain.MemberRepository;
import com.zerobase.Cafe_JOO.front.order.domain.Order;
import com.zerobase.Cafe_JOO.front.order.domain.OrderProduct;
import com.zerobase.Cafe_JOO.front.order.domain.OrderProductRepository;
import com.zerobase.Cafe_JOO.front.order.domain.OrderRepository;
import com.zerobase.Cafe_JOO.front.review.domain.Review;
import com.zerobase.Cafe_JOO.front.review.domain.ReviewRepository;
import com.zerobase.Cafe_JOO.front.review.dto.ReviewAddDto.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.zerobase.Cafe_JOO.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final MemberRepository memberRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    private final S3UploaderService s3UploaderService;

    private final TokenProvider tokenProvider;

    // 리뷰 등록
    public void addReview(String token, MultipartFile image, Request request)
        throws IOException {
        // 1. 사용자 인증
        Long memberIdByToken = tokenProvider.getId(token);
        // 2. 인증된 사용자(id)로 회원 조회 = memberByToken
        Member memberByToken = memberRepository.findById(memberIdByToken)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_EXISTS));
        // 3. 찾은 회원으로 해당 회원이 주문한 상품 조회 = orderProduct
        OrderProduct orderProduct = orderProductRepository.findById(request.getOrderProductId())
                .orElseThrow(() -> new CustomException(ORDER_PRODUCT_NOT_EXISTS));

        // 4. 찾은 주문상품에서 주문 내역의 id로 조회 = order
        Order order = orderRepository.findById(orderProduct.getOrderId())
                .orElseThrow(() -> new CustomException(ORDER_NOT_EXISTS));
        // memberIdByToken != null, 주문.회원.회원 id 와 같으면 exception
        if (!memberIdByToken.equals(order.getMember().getId())) {
            throw new CustomException(ORDER_PRODUCT_NOT_MATCH_MEMBER);
        }

        // orderProduct 기준으로 리뷰 중복을 검사. (orderProduct는 특정 사용자와 연결된 정보)
        // Order 객체 안에 Member(구매자)와 연결이 되어 있으므로, 중복 구분 가능.
        reviewRepository.findByOrderProduct(orderProduct)
                .ifPresent(review -> {
                    throw new CustomException(REVIEW_ALREADY_WRITTEN);
                });

        String s3UploadedUrl = s3UploaderService.uploadFileToS3(image, "dirName");
        reviewRepository.save(Review.from(memberByToken, orderProduct, request, s3UploadedUrl));
    }
}
