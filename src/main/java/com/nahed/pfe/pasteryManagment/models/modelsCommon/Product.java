package com.nahed.pfe.pasteryManagment.models.modelsCommon;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@MappedSuperclass
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id ;

    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    private ProductUnitEnum unit;
    private BigDecimal quantity;

    @NotNull
    @Column(name="stock_limit")
    private BigDecimal stockLimit;

}
