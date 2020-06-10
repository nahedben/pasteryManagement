package com.nahed.pfe.pasteryManagment.models.modelsStock;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nahed.pfe.pasteryManagment.models.modelsCommon.MatierePremiere;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Famille {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String  description;

    @OneToMany(cascade = CascadeType.ALL,targetEntity = MatierePremiere.class, mappedBy="famille")
    @JsonIgnore
    private List matierePremieres;
}
