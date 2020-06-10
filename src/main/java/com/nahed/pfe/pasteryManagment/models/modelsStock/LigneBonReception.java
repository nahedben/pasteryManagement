package com.nahed.pfe.pasteryManagment.models.modelsStock;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nahed.pfe.pasteryManagment.models.modelsCommon.MatierePremiere;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name="ligne_bon_reception")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LigneBonReception {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private BigDecimal quantity;

    @Column(name="totalprice")
    private BigDecimal total;
    private boolean qtyInserted;
    private boolean totalAffected;

    @ManyToOne
    @JoinColumn(name="matierepremier_id")
    private MatierePremiere matierepremier;

    @ManyToOne
    @JoinColumn(name="bonreception_id")
    private BonReception bonReception;

}
