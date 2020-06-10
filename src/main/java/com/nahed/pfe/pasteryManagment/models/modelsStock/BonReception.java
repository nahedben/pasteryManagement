package com.nahed.pfe.pasteryManagment.models.modelsStock;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class BonReception {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="numero_bon_reception")
    private String numBonRecep;

    @Column(name="date_bon_reception")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date dateBr;

    @Column(name="nom_livreur")
    private String nomLivreur;
    @Column(name="mat_livreur")
    private String matLivreur;
    private String note;


    @Column(name="montant_tot_ht")
    private BigDecimal montTotHt;

    @Column(name="montant_tot_ttc")
    private BigDecimal montTotTtc;

    @ManyToOne
    @JoinColumn(name="fournisseur_id")
    private Fournisseur fournisseur;

    @OneToMany(cascade = CascadeType.ALL,targetEntity = LigneBonReception.class,
                mappedBy ="bonReception", orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    private List lignebonreceptions;




}
