package com.zerobase.Cafe_JOO.front.product.domain;

import com.zerobase.Cafe_JOO.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class OptionCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String name;

    public void modifyOptionCategoryDto(String optionCategoryDtoName) {
        this.name = optionCategoryDtoName;
    }
}

