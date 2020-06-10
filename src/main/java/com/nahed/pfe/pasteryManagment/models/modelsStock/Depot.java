package com.nahed.pfe.pasteryManagment.models.modelsStock;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nahed.pfe.pasteryManagment.models.modelsCommon.MatierePremiere;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Depot {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long  id;
    private String name;
    private String details;
    private String location;
    private BigDecimal capacity;
    private BigDecimal actualCharge;

    @OneToMany(cascade = CascadeType.ALL,targetEntity = MatierePremiere.class,mappedBy = "depot")
    @JsonIgnore
   private List matierePremieres;

}
