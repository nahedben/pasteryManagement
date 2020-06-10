package com.nahed.pfe.pasteryManagment.repositories.stockRepositories;

import com.nahed.pfe.pasteryManagment.models.modelsStock.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FournisseurRepository extends JpaRepository<Fournisseur,Long> {
}
