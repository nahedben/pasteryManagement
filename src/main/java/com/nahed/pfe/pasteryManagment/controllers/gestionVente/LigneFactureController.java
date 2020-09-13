package com.nahed.pfe.pasteryManagment.controllers.gestionVente;

import com.nahed.pfe.pasteryManagment.models.modelVente.Facture;
import com.nahed.pfe.pasteryManagment.models.modelVente.LigneFacture;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Produit;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.ProduitRepository;
import com.nahed.pfe.pasteryManagment.repositories.venteRepositories.FactureRepository;
import com.nahed.pfe.pasteryManagment.repositories.venteRepositories.LigneFactureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("api/v1")
public class LigneFactureController {
    @Autowired
    private LigneFactureRepository ligneFactureRepository;
@Autowired
private FactureRepository factureRepository;

    @GetMapping("/ligneFacture/fct/{idfct}")
    public List<LigneFacture> retreiveLgnFctFccture(@PathVariable Long idfct){
//        List<LigneFacture> lst=ligneFactureRepository.findAll();
//        System.out.println("La liste lgn:"+lst);
//        List<LigneFacture> lstsearch= new ArrayList<>();
//        for(LigneFacture e : lst){
//           Long idf= e.getFacture().getId();
//           if(idf == idfct) lstsearch.add(e);
//
//        }
//        return lstsearch;


        return ligneFactureRepository.findAllByFactureId(idfct);
    }

//       List<LigneFacture> lst=ligneFactureRepository.findAllByFactureId(idfct);
//       System.out.println("La liste des lignee"+lst);
//       return lst;

    @DeleteMapping("/ligneFacture/{id}")
    public void deleteLgFact(@PathVariable Long id){
        ligneFactureRepository.deleteById(id);
    }

    @PostMapping("/ligneFacture")
    public LigneFacture createLigneFacture(@RequestBody LigneFacture lgnFact){
        return ligneFactureRepository.saveAndFlush(lgnFact);
    }
    @GetMapping("/ligneFactures")
    public List<LigneFacture> retreiveAllLigneFacture(){
        return ligneFactureRepository.findAll();
    }

    @GetMapping("/ligneFacture/{id}")
    public ResponseEntity<LigneFacture> retriveLigneFacture(@PathVariable Long id){
        Optional<LigneFacture> lf= ligneFactureRepository.findById(id);
        if(lf.isPresent())
            return ResponseEntity.ok(lf.get());
        return ResponseEntity.notFound().build();
    }

    @Autowired
    private ProduitRepository produitRepository;

    /**
     * Assign Produit to LigneFacture
     * @param idLFct
     * @param idPrd
     * @return
     */
    @PutMapping("/ligneFacture/{idLFct}/assignProduit/{idPrd}")
    public LigneFacture assignProduit(@PathVariable Long idLFct, @PathVariable Long idPrd){
        LigneFacture lft=ligneFactureRepository.getOne(idLFct);
        Produit prd=produitRepository.getOne(idPrd);
        lft.setProduit(prd);
        return ligneFactureRepository.saveAndFlush(lft);
    }

    /**
     *Methode de calcule de TotalPrice de LigneFacture
     * @param id
     * @return
     */

    @PutMapping("/ligneFacture/{id}/calculTotalPrice")
    public LigneFacture calculeToalPriceLigne(@PathVariable Long id){
        LigneFacture lf= ligneFactureRepository.getOne(id);
        BigDecimal Qt= lf.getQuantity();
        Produit prd=lf.getProduit();
        BigDecimal unitPrice=prd.getUnitPrice();
        BigDecimal totalPrice=Qt.multiply(unitPrice);
        //allouer le TotalPrice a ligneFacture
        lf.setTotalPrice(totalPrice);
        return ligneFactureRepository.saveAndFlush(lf);
    }

    /**
     * Methode qui nous permet de modifier la quantity de LignFacture et modifier le totalPrice de la ligneFacture
     * dans le cas ou cette derniere n'est pas null;
     * @param lf
     * @param id
     * @return
     */

    @PutMapping("ligneFacture/{id}")
    public LigneFacture editerQtyLigneFacture(@RequestBody LigneFacture lf, @PathVariable Long id ){
        LigneFacture ExistLf= ligneFactureRepository.getOne(id);
        ExistLf.setQuantity(lf.getQuantity());
        ligneFactureRepository.saveAndFlush(ExistLf);
        if(ExistLf.getTotalPrice()!=null){
        calculeToalPriceLigne(ExistLf.getId());}
        return ligneFactureRepository.saveAndFlush(ExistLf);
    }

//    @Autowired
//    private FactureRepository factureRepository;
    @PutMapping("ligneFacture/{idlgfc}/AssignFacture/{idfc}")
    public LigneFacture AssignFactureToLignFct(@PathVariable Long idlgfc, @PathVariable Long idfc){
        LigneFacture lfct= ligneFactureRepository.getOne(idlgfc);
        Facture fct= factureRepository.getOne(idfc);
        lfct.setFacture(fct);
        return ligneFactureRepository.saveAndFlush(lfct);
    }
}
