package com.zerobase.Cafe_JOO.front.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOptionCategoryRepository extends
        JpaRepository<ProductOptionCategory, Integer> {

    List<ProductOptionCategory> findAllByProductId(Integer productId);

}
