package com.zerobase.Cafe_JOO.front.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionCategoryRepository extends JpaRepository<OptionCategory, Integer> {

}
