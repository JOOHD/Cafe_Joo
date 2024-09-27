package com.zerobase.Cafe_JOO.front.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductOptionRepository extends JpaRepository<OrderProductOption, Long> {

}
