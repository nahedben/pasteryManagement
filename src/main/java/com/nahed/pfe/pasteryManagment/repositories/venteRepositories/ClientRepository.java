package com.nahed.pfe.pasteryManagment.repositories.venteRepositories;

import com.nahed.pfe.pasteryManagment.models.modelVente.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
//    @Query("select c from Client c where c.phone = :phone")
    List<Client> findByPhone(String ph);
}
