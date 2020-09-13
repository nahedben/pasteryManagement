package com.nahed.pfe.pasteryManagment.models.modelsCommon;

import com.nahed.pfe.pasteryManagment.models.modelsProd.LigneRecette;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Produit;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Recette;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class CompletProduit {

        private Recette recette;
        private Produit produit;
        private List<LigneRecette> listLigneRecette;

}
