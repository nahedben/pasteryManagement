package com.nahed.pfe.pasteryManagment.models.modelsProd;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class OrdreProduction {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(insertable= true, updatable=false)
    private LocalDateTime created;
    private boolean debutProd;
//    @Column(insertable= true, updatable=false)
    private LocalDateTime dateLancement;
    private boolean prodFinish;
    private LocalDateTime sortieProd;
    private long tempsPrd;


    @NotNull
    private BigDecimal quantity;
    private BigDecimal qtSortieProd;

    @JsonFormat(pattern="dd-MM-yyyy")
    private Date SortiePrevisionnel;

    @ManyToOne
    @JoinColumn(name="recette_id")
    private Recette recette;

    //cette relation est eliminer pour revenir a la premiere conception
//    @ManyToOne
//    @JoinColumn(name="produit_id")
//    private Produit produit;

    @PrePersist
    void onCreated() {
        this.setCreated(LocalDateTime.now());
    }


}
