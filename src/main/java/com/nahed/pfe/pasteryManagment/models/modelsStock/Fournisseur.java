package com.nahed.pfe.pasteryManagment.models.modelsStock;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name="datecreation")
    private Date dateCreation;
    private String name;
    private String email;
    private String phone;
    private String address;
    private BigDecimal compte;
    private Integer tax;
    @Column(name="contact_name")
    private String contactName;

    private String note;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = BonReception.class,mappedBy = "fournisseur")
    @JsonIgnore
    private List bonreceptions;

}
