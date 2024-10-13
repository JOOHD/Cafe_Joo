package com.zerobase.Cafe_JOO.front.review.controller;

import com.zerobase.Cafe_JOO.front.review.dto.ReviewAddDto;
import com.zerobase.Cafe_JOO.front.review.dto.ReviewAddForm;
import com.zerobase.Cafe_JOO.front.review.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(name = "review-controller", description = "리뷰 CRUD API")
@RestController
@RequestMapping("/auth/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @ApiOperation(value = "리뷰 등록", notes = "사용자는 별점, 내용, 사진으로 리뷰를 등록할 수 있습니다.")
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> reviewAdd(
        @RequestHeader(name = "Authorization") String token,
        @RequestParam(value = "image")MultipartFile image,
        @Valid ReviewAddForm.Request reviewAddForm) throws IOException {
        reviewService.addReview(token, image, ReviewAddDto.Request.from(reviewAddForm));
        return ResponseEntity.status(CREATED).build();
    }
}
