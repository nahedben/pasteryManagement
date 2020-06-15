package com.nahed.pfe.pasteryManagment.repositories.venteRepositories;

import com.nahed.pfe.pasteryManagment.models.modelVente.Facture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactureRepository extends JpaRepository<Facture,Long> {
}
