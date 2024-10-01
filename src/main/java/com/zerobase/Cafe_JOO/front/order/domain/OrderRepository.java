package com.zerobase.Cafe_JOO.front.order.domain;

import com.zerobase.Cafe_JOO.common.type.OrderReceiptStatus;
import com.zerobase.Cafe_JOO.front.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByMemberAndCreatedDateAfter(Member member, LocalDateTime createdDate);

    List<Order> findByMember(Member member);

    List<Order> findByMemberAndCreatedDateBetween(Member member, LocalDateTime startDate, LocalDateTime endDate);

    List<Order> findByReceiptStatus(OrderReceiptStatus receiptStatus);
}
