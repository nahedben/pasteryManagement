package com.nahed.pfe.pasteryManagment.repositories.stockRepositories;

import com.nahed.pfe.pasteryManagment.models.modelsCommon.MatierePremiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatierePremierRepository extends JpaRepository<MatierePremiere,Long> {
    List<MatierePremiere> findAllByFamilleId(Long  id);
    List<MatierePremiere> findAllByDepotId(Long id);

    //native query using for getting all matierePremiere with stock limit alert
    @Query(value="SELECT * FROM matiere_premiere AS mt WHERE mt.stock_limit_alert=true", nativeQuery = true)
    List<MatierePremiere> listMatPremWithStokAlert();

    @Query(value="SELECT * FROM matiere_premiere AS mt WHERE mt.quantity<= mt.stock_limit",nativeQuery =true)
    List<MatierePremiere> listMatWithqtlessLmt();
}
