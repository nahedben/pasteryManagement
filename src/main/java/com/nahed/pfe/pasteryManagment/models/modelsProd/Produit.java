package com.nahed.pfe.pasteryManagment.models.modelsProd;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nahed.pfe.pasteryManagment.models.modelsCommon.Product;
import com.nahed.pfe.pasteryManagment.models.modelVente.LigneBonCommande;
import com.nahed.pfe.pasteryManagment.models.modelVente.LigneFacture;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Produit extends Product {

    @Column(name="percentage_margin")
    private Integer percentageMargin;
    private BigDecimal unitPrice;
    private boolean unitPriceValidation;

    @Column(name="produit_coast",precision = 18,scale = 3)
    private BigDecimal prodCoast;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    private byte[] photo;

//    @ManyToMany(cascade = CascadeType.ALL,targetEntity = Recette.class)
//    @JoinTable(name="produit_recette",
//                joinColumns = @JoinColumn(name = "produit_id",referencedColumnName = "id"),
//                inverseJoinColumns = @JoinColumn(name = "recette_id", referencedColumnName = "id"))
//    private List recette;

    @OneToMany(cascade = CascadeType.ALL,targetEntity = LigneFacture.class,mappedBy = "produit")
    @JsonIgnore
    private List ligneFacture;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = LigneBonCommande.class, mappedBy="produit")
    @JsonIgnore
    private List ligneBonCommande;
    /**
     * c'est ma deuxieme modification de mettre en place une relation OnToMany entre Produit et Ordre de Production
     */
//    @OneToMany(cascade = CascadeType.ALL, targetEntity = OrdreProduction.class, mappedBy="produit")
//    @JsonIgnore
//    private List ordreProduction;

//    @OneToMany(cascade =CascadeType.ALL, targetEntity = CoefRecProd.class, mappedBy = "produit")
//    @JsonIgnore
//    private List coefRecProd;

    @OneToOne(mappedBy = "produit")
    private Recette recette;



}
