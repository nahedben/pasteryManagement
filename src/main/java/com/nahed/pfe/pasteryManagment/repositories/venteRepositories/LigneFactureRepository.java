package com.nahed.pfe.pasteryManagment.repositories.venteRepositories;
import com.nahed.pfe.pasteryManagment.models.modelVente.LigneFacture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigneFactureRepository extends JpaRepository<LigneFacture,Long> {
    List<LigneFacture> findAllByFactureId(Long id);

}

