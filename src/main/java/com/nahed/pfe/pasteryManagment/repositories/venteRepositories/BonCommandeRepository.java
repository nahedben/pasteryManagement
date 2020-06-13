package com.nahed.pfe.pasteryManagment.repositories.venteRepositories;

import com.nahed.pfe.pasteryManagment.models.modelVente.BonCommande;
import com.nahed.pfe.pasteryManagment.models.modelVente.LigneBonCommande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BonCommandeRepository extends JpaRepository<BonCommande,Long> {
}
