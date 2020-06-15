package com.nahed.pfe.pasteryManagment.models.modelVente;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Facture {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(insertable = true, updatable = false)
    private LocalDateTime dateFacture;

    private boolean Factvalidation;
    private BigDecimal taxValue;
    private BigDecimal totalPriceTtc;

    @Enumerated(EnumType.STRING)
    private TypePaymentEnum typePayment;

    private boolean Regelement;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = LigneFacture.class, mappedBy = "facture")
    @JsonIgnore
    private List ligneFacture;

    @ManyToOne
    @JoinColumn(name="client_id")
    private Client client;

    @PrePersist
    void onDateFacture(){
        this.setDateFacture(LocalDateTime.now());
    }






}
