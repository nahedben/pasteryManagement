package com.nahed.pfe.pasteryManagment.models.modelsProd;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Recette {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String designation;
    //c'est le nombre de piece que peut produire la recette
    private Integer recettePour;
    @Column(insertable = true, updatable = false)
    private LocalDateTime dateCreation;

    @Column(precision = 18,scale = 3)
    private BigDecimal recetteCoast;

    @OneToMany(cascade=CascadeType.ALL,targetEntity = LigneRecette.class,
            mappedBy ="recette", orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List ligneRecette;


    @OneToMany(cascade = CascadeType.ALL, targetEntity = OrdreProduction.class,mappedBy = "recette")
    @JsonIgnore
    private List ordreProd;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="produit_id")
//    @JsonIgnore
    @JsonView(View.Summary.class)
    private Produit produit;
    /***
     * je viens d'eliminer cette relation entre recette et OrdreProduction
     * et la remplacer avec Produit et OrdreProduction puisque un produit peut avoir plusieurs recette.
     */

    @PrePersist
        void onDateCreation(){
        this.setDateCreation(LocalDateTime.now());
    }




}
