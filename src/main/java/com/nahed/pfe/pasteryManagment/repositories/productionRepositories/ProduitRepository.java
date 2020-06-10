package com.nahed.pfe.pasteryManagment.repositories.productionRepositories;

import com.nahed.pfe.pasteryManagment.models.modelsProd.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit,Long> {
}
