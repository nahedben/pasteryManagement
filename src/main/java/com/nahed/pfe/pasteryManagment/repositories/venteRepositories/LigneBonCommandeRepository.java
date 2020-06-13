package com.nahed.pfe.pasteryManagment.repositories.venteRepositories;

import com.nahed.pfe.pasteryManagment.models.modelVente.LigneBonCommande;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.event.ListDataEvent;
import java.util.List;

public interface LigneBonCommandeRepository extends JpaRepository<LigneBonCommande,Long> {
    List<LigneBonCommande> findAllByBonCommandeId(Long id);
}
