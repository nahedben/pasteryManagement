package com.nahed.pfe.pasteryManagment.repositories.productionRepositories;

import com.nahed.pfe.pasteryManagment.models.modelsProd.LigneRecette;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigneRecetteRepository extends JpaRepository<LigneRecette,Long> {
    List<LigneRecette> findAllByRecetteId(Long id);
}
