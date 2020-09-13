package com.nahed.pfe.pasteryManagment.models.modelsCommon;

import com.fasterxml.jackson.annotation.JsonView;
import com.nahed.pfe.pasteryManagment.models.modelsProd.View;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@MappedSuperclass
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @JsonView(View.Summary.class)
    private Long id ;

    @NotNull
    @JsonView(View.Summary.class)
    private String name;

    @Enumerated(EnumType.STRING)
    @JsonView(View.Summary.class)
    private ProductUnitEnum unit;
    @JsonView(View.Summary.class)
    private BigDecimal quantity;

    @NotNull
    @Column(name="stock_limit")
    @JsonView(View.Summary.class)
    private BigDecimal stockLimit;

}
