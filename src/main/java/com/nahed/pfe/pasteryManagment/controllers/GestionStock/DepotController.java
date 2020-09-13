package com.nahed.pfe.pasteryManagment.controllers.GestionStock;

import com.nahed.pfe.pasteryManagment.models.modelsStock.Depot;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.DepotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("api/v1")
public class DepotController {
    @Autowired
    private DepotRepository depotRepository;

    @GetMapping("/depots")
    public List<Depot> findAllDepot() {
        return depotRepository.findAll();
    }

    @GetMapping("/depot/{id}")
    public Depot findDepotWithID(@PathVariable Long id) {
        return depotRepository.getOne(id);


    }

    /***
     * creation d'une nouveau depot en utilisant les attributs:
     * "name", "details", "location","capacite"
     * @param depot
     * @return
     */
    @PostMapping("/depot")
    public Depot addNewdepot(@RequestBody Depot depot) {
        depotRepository.save(depot);
        depot.setActualCharge(null);
        return depotRepository.saveAndFlush(depot);
    }

    /***
     * editer depot on the attributes  name, details, location,capacity
     * the actual charge attribute is automated by the quantity on the MatierePremiere
     * @param depot
     * @param id
     * @return
     */
    @PutMapping("/depot/{id}")
    public Depot editDepot(@RequestBody Depot depot, @PathVariable Long id) {
        Depot existDp = depotRepository.getOne(id);
        if(depot.getName()!=null){existDp.setName(depot.getName());}
        if(depot.getDetails()!=null){existDp.setDetails(depot.getDetails());}
        if(depot.getLocation()!=null){existDp.setLocation(depot.getLocation());}
        if(depot.getCapacity()!=null){existDp.setCapacity(depot.getCapacity());}
        return depotRepository.saveAndFlush(existDp);
    }

    /***
     * with this method we are able to edit actualCharge but it should be with an access to it
     * since this attribute have to be updated automatically with matierePremiere quantity
     * @param dp
     * @param id
     * @return
     */
    @PutMapping("/depot/actualCharge/{id}")
    public Depot editActCharge(@RequestBody Depot dp, @PathVariable Long id){
        Depot ExistDp= depotRepository.getOne(id);
        ExistDp.setActualCharge(dp.getActualCharge());
        return depotRepository.saveAndFlush(ExistDp);
    }

    @DeleteMapping("/depot/{id}")
    public void removeDepot(@PathVariable Long id) {
        depotRepository.deleteById(id);

    }
}
