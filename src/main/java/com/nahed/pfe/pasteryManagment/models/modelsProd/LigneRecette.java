package com.nahed.pfe.pasteryManagment.models.modelsProd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nahed.pfe.pasteryManagment.models.modelsCommon.MatierePremiere;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LigneRecette {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(precision = 18, scale=3)
    private BigDecimal consommation;

    @Column(precision =18, scale=3)
    private BigDecimal coastLine;

//    @Column(precision = 18, scale=3)
//    private BigDecimal consoUnit;

    @ManyToOne
    @JoinColumn(name="matierepremier_id")
    private MatierePremiere matierepremier;

   @ManyToOne
   @JoinColumn(name="recette_id")
   private Recette recette;

}
