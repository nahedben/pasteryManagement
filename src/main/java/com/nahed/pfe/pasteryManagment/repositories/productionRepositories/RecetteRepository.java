package com.nahed.pfe.pasteryManagment.repositories.productionRepositories;

import com.nahed.pfe.pasteryManagment.models.modelsProd.Produit;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Recette;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecetteRepository extends JpaRepository<Recette,Long> {
    Produit findProduitById(Long id);
}
