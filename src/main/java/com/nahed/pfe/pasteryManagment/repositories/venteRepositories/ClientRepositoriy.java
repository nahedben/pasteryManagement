package com.nahed.pfe.pasteryManagment.repositories.venteRepositories;

import com.nahed.pfe.pasteryManagment.models.modelVente.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  ClientRepositoriy extends JpaRepository<Client, Long> {
}
