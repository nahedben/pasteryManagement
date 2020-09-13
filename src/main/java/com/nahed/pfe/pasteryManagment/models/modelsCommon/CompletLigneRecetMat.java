package com.nahed.pfe.pasteryManagment.models.modelsCommon;

import com.nahed.pfe.pasteryManagment.models.modelsProd.LigneRecette;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class CompletLigneRecetMat {
    private LigneRecette ligneRecette;
    private MatierePremiere matierePremiere;
}
