package com.nahed.pfe.pasteryManagment.models.modelsProd;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.nahed.pfe.pasteryManagment.models.modelsCommon.Product;
import com.nahed.pfe.pasteryManagment.models.modelVente.LigneBonCommande;
import com.nahed.pfe.pasteryManagment.models.modelVente.LigneFacture;
import com.nahed.pfe.pasteryManagment.models.modelsStock.Famille;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.transaction.annotation.Transactional;

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
    @JsonView(View.Summary.class)
    private Integer percentageMargin;
    @JsonView(View.Summary.class)
    private BigDecimal unitPrice;
    @JsonView(View.Summary.class)
    private boolean unitPriceValidation;

    @Column(name="produit_coast",precision = 18,scale = 3)
    @JsonView(View.Summary.class)
    private BigDecimal prodCoast;

    @Basic(fetch = FetchType.LAZY)
    @Lob
//    @Type(type = "org.hibernate.type.ImageType")
    @Column(columnDefinition = "LONGVARBINARY")
    @JsonView(View.Summary.class)
    private byte[] photo;

    @OneToMany(cascade = CascadeType.ALL,targetEntity = LigneFacture.class,mappedBy = "produit")
    @JsonIgnore
    private List ligneFacture;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = LigneBonCommande.class, mappedBy="produit")
    @JsonIgnore
    private List ligneBonCommande;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "produit")
@JsonIgnore
    private Recette recette;

    @ManyToOne
    @JoinColumn(name = "famille_id")
    @JsonView(View.Summary.class)
    private Famille famille;



}
