package com.nahed.pfe.pasteryManagment.models.modelVente;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BonCommande {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(insertable= true, updatable=false)
    private LocalDateTime dateCreation;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime dateSouhaite;
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date dateLivraisonPrevisionnel;
    private BigDecimal totalPric;
    private BigDecimal Avance;
    private BigDecimal RestPaye;
    private String infoSupp;
    private long tempsRestantLiv;
    private boolean BlFacturer;

    @ManyToOne
    @JoinColumn(name="client_id")
    private Client client;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = LigneBonCommande.class, mappedBy="bonCommande")
    @JsonIgnore
    private List ligneBonCommande;

    @PrePersist
    void onDateCreation(){
        this.setDateCreation(LocalDateTime.now());

    }



}
