package com.nahed.pfe.pasteryManagment.models.modelVente;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Client {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime datePremAchat;
    private String nom;
    private String prenom;
    private String Email;
    private String phone;
    private String address;
    private String tax;
    private BigDecimal chiffreAchat;
    private BigDecimal credit;
    private String note;

    @OneToMany(cascade = CascadeType.ALL, targetEntity= Facture.class, mappedBy= "client")
    @JsonIgnore
    private List facture;

    @OneToMany(cascade = CascadeType.ALL, targetEntity=BonCommande.class, mappedBy="client")
    @JsonIgnore
    private List bonCommande;

}
