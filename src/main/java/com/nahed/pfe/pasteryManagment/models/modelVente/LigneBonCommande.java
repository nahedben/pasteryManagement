package com.nahed.pfe.pasteryManagment.models.modelVente;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Produit;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LigneBonCommande {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private BigDecimal quantity;
    @Column(precision=18, scale = 3)
    private BigDecimal totalLinePrice;

    @ManyToOne
    @JoinColumn(name="produit_id")
    private Produit produit;

    @ManyToOne
    @JoinColumn(name="bonCommande_id")
    private BonCommande bonCommande;

}
