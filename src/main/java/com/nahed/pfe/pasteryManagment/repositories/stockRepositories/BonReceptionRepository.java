package com.nahed.pfe.pasteryManagment.repositories.stockRepositories;

import com.nahed.pfe.pasteryManagment.models.modelsStock.BonReception;
import com.nahed.pfe.pasteryManagment.models.modelsStock.LigneBonReception;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BonReceptionRepository extends JpaRepository<BonReception,Long> {
    List<BonReception> findAllByFournisseurId(Long id);


}
