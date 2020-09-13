package com.nahed.pfe.pasteryManagment.controllers.GestionStock;

import com.nahed.pfe.pasteryManagment.models.modelsStock.Depot;
import com.nahed.pfe.pasteryManagment.models.modelsStock.Famille;
import com.nahed.pfe.pasteryManagment.models.modelsCommon.MatierePremiere;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.DepotRepository;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.FamilleRepository;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.MatierePremierRepository;
import com.nahed.pfe.pasteryManagment.services.MatierePremiereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1")
public class MatierePremiereController {
    @Autowired
    private MatierePremierRepository matierePremierRepository;
    @Autowired
    private MatierePremiereService matierePremiereService;

    @GetMapping("/matprems")
    public List<MatierePremiere> findAllupdated() {
//        matierePremiereService.updatedStockAlertList();
        return matierePremierRepository.findAll();
    }
    //Retrieving a specific MatierPremier by its Id
    @GetMapping("/matprem/{id}")
    public MatierePremiere findMatPremWithID(@PathVariable Long id) {
        return matierePremierRepository.getOne(id);
    }

    //Retreiving all the Matiere in a specific Depot
    @GetMapping("matprems/depot/{id}")
    public List<MatierePremiere> findMatPremByDepotId(@PathVariable Long id){
        return matierePremierRepository.findAllByDepotId(id);
    }

    /***
     * Aves la methode addNewMatPrem(mat) nous allons pouvoir creer dans la base une nouvelle instance de matierepremier
     * en utilisant les attributs name,unit,stockLimit,purchaseUnitPrice
     * @param mat
     * @return
     */
    @PostMapping("/matprem")
    public MatierePremiere addNewMatPrem(@RequestBody MatierePremiere mat) {
        matierePremierRepository.save(mat);
        mat.setDepot(null);
//        mat.setFamille(null);
        mat.setQuantity(null);
        mat.setLigneBonReceptions(null);
        return matierePremierRepository.saveAndFlush(mat);
    }

    /****
     * updating data on the attributes purchaseUnitPrice And unit
     * @param mat
     * @param id
     * @return
     */
    @PutMapping("/matprem/{id}")
    public MatierePremiere editMatPrem(@RequestBody MatierePremiere mat, @PathVariable Long id) {
        MatierePremiere matpremExist =matierePremierRepository.getOne(id);
        //matpremExist.setName(mat.getName());
        if(mat.getUnit()!=null){matpremExist.setUnit(mat.getUnit());}
        //matpremExist.setStockLimit(mat.getStockLimit());
        if(mat.getPurchaseUnitPrice()!=null){matpremExist.setPurchaseUnitPrice(mat.getPurchaseUnitPrice());}
         return matierePremierRepository.saveAndFlush(matpremExist);
    }

    /***
     * updating matierePremiere on the attributes "name" and "stockLimit"
     * I have made a separate update since they are declared Not Null
     * @param mat
     * @param id
     * @return
     */
    @PutMapping("/matprem/name/stLimit/{id}")
    public MatierePremiere editParamNamSlim(@RequestBody MatierePremiere mat, @PathVariable Long id){
        MatierePremiere matpremExist = matierePremierRepository.getOne(id);
        if(mat.getName()!=null){matpremExist.setName(mat.getName());}
        if(mat.getStockLimit()!=null){matpremExist.setStockLimit(mat.getStockLimit());}
        return matierePremierRepository.saveAndFlush(matpremExist);
    }

    /**
     * with this method we only can modify the "Quantity" and this is a very dangerous method since from it we are
     * able to act manually on stock and it would be made only with an access on the front end
     * @param mat
     * @param id
     * @return
     */
    @PutMapping("/matprem/qty/{id}")
    public MatierePremiere editMatPremQty(@RequestBody MatierePremiere mat, @PathVariable Long id){
        MatierePremiere mtExist=matierePremierRepository.getOne(id);
        mtExist.setQuantity(mat.getQuantity());
        return matierePremierRepository.saveAndFlush(mtExist);
    }
    @Autowired
    private DepotRepository depotRepository;

    /**
     * assign Depot to matierePremiere
     * here this method actualise the "actualcharge" of Depot
     * @param idmt
     * @param iddepot
     * @return
     */
    @PutMapping("matprem/assignDepot/{idmt}/{iddepot}")
    public MatierePremiere assignDepot(@PathVariable Long idmt, @PathVariable Long iddepot){
        MatierePremiere mt =matierePremierRepository.getOne(idmt);
//        System.out.println(mt);
        Depot dp=depotRepository.getOne(iddepot);
//        System.out.println(dp);
//        System.out.println(dp.getActualCharge());
//        System.out.println(mt.getQuantity());
        BigDecimal initVal = new BigDecimal("0");
        BigDecimal initValMt =new BigDecimal("0");
        System.out.println(initVal +" "+initValMt);
            if (mt.getQuantity()==null ) {
                mt.setQuantity(initValMt);
                if(dp.getActualCharge() == null){
                    dp.setActualCharge(initVal);}
                System.out.println("the quantity value"+mt.getQuantity());
                System.out.println("the actualCharge"+dp.getActualCharge());
            dp.setActualCharge(dp.getActualCharge().add(mt.getQuantity()));
            }
        List<MatierePremiere> lstMt=dp.getMatierePremieres();
        lstMt.add(mt);
        dp.setMatierePremieres(lstMt);
        depotRepository.saveAndFlush(dp);
        mt.setDepot(dp);
        return matierePremierRepository.saveAndFlush(mt);
    }

//    @Autowired
//    private FamilleRepository familleRepository;


//    @PutMapping("matprem/assignFamille/{idmt}/{idFam}")
//    public MatierePremiere assignFamille(@PathVariable Long idmt, @PathVariable Long idFam){
//        MatierePremiere mt = matierePremierRepository.getOne(idmt);
//        Famille fm= familleRepository.getOne(idFam);
//        List<MatierePremiere> lstMt= fm.getMatierePremieres();
//        lstMt.add(mt);
//        fm.setMatierePremieres(lstMt);
//        mt.setFamille(fm);
//        familleRepository.saveAndFlush(fm);
//        return matierePremierRepository.saveAndFlush(mt);
//    }


    //delete data link
    @DeleteMapping("/matprem/{id}")
    public void removeMatPrem(@PathVariable Long id) {
        matierePremierRepository.deleteById(id);
    }

}
