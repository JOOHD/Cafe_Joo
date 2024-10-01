package com.zerobase.Cafe_JOO.front.cart.domain;

import com.zerobase.Cafe_JOO.common.type.CartOrderStatus;
import com.zerobase.Cafe_JOO.front.member.domain.Member;
import com.zerobase.Cafe_JOO.front.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findAllByMember(Member member);

    List<Cart> findAllByMemberAndStatus(Long memberId, CartOrderStatus status);

    void deleteAllByMember(Member member);

    List<Cart> findAllByMemberAndStatus(Member member, CartOrderStatus status);

    List<Cart> findByMemberAndProduct(Member member , Product product);

    List<Cart> findByMember(Member member);
}
