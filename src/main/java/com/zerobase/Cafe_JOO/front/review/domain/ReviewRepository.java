package com.zerobase.Cafe_JOO.front.review.domain;

import com.zerobase.Cafe_JOO.front.order.domain.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByOrderProduct(OrderProduct orderProduct);
}
