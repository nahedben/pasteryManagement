package com.nahed.pfe.pasteryManagment.repositories.stockRepositories;

import com.nahed.pfe.pasteryManagment.models.modelsStock.LigneBonReception;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigneBonReceptionRepository extends JpaRepository<LigneBonReception,Long> {
    List<LigneBonReception> findAllByBonReceptionId(Long id);
}
