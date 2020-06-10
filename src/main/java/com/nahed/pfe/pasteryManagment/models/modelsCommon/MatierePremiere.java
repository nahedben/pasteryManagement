package com.nahed.pfe.pasteryManagment.models.modelsCommon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nahed.pfe.pasteryManagment.models.modelsProd.LigneRecette;
import com.nahed.pfe.pasteryManagment.models.modelsStock.Depot;
import com.nahed.pfe.pasteryManagment.models.modelsStock.Famille;
import com.nahed.pfe.pasteryManagment.models.modelsStock.LigneBonReception;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name="matiere_premiere")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MatierePremiere extends Product {


    @Column(name = " purchase_unit_price")
    private BigDecimal purchaseUnitPrice;

//    @Column(name = " stock_limit_alert")
//    private boolean stockLimitAlert;


    @ManyToOne
    @JoinColumn(name = "depot_id")
    private Depot depot;

    @ManyToOne
    @JoinColumn(name = "famille_id")
    private Famille famille;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = LigneBonReception.class, mappedBy = "matierepremier")
    @JsonIgnore
    private List ligneBonReceptions;

    @OneToMany(cascade=CascadeType.ALL, targetEntity = LigneRecette.class, mappedBy="matierepremier")
    @JsonIgnore
    private List ligneRecette;

   }


