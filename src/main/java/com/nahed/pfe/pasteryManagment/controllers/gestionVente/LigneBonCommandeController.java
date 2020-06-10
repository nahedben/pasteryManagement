package com.nahed.pfe.pasteryManagment.controllers.gestionVente;

import com.nahed.pfe.pasteryManagment.models.modelVente.BonCommande;
import com.nahed.pfe.pasteryManagment.models.modelVente.LigneBonCommande;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Produit;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.ProduitRepository;
import com.nahed.pfe.pasteryManagment.repositories.venteRepositories.BonCommandeRepository;
import com.nahed.pfe.pasteryManagment.repositories.venteRepositories.LigneBonCommandeRepository;
import org.hibernate.sql.AliasGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LigneBonCommandeController {
    @Autowired
    private LigneBonCommandeRepository ligneBonCommandeRep;

    @GetMapping("/ligneBCs")
    public List<LigneBonCommande> retreiveAllBc(){
        return ligneBonCommandeRep.findAll();
    }
    @GetMapping("/ligneBC/{id}")
    public LigneBonCommande retireiveLigneBcById(@PathVariable Long id){
        return ligneBonCommandeRep.getOne(id);
    }
    @PostMapping("/ligneBC")
    public LigneBonCommande createLignBC(@RequestBody LigneBonCommande lbc){

        return ligneBonCommandeRep.saveAndFlush(lbc);
    }
    @Autowired
    private ProduitRepository produitRepository;
    @PutMapping("/ligneBc/AssignProduit/{idlbc}/{idproduit}")
    public LigneBonCommande AssignProduitToLbc(@PathVariable Long idlbc,@PathVariable Long idproduit){
        LigneBonCommande ExistLbc =ligneBonCommandeRep.getOne(idlbc);
        Produit AssignProd=produitRepository.getOne(idproduit);
        ExistLbc.setProduit(AssignProd);
        return ligneBonCommandeRep.saveAndFlush(ExistLbc);
    }

    /**
     * THIS METHOD IS TO Assign Produit to LigneBonCommande
     *
     * @param id
     * @return
     */
    @PutMapping("/ligneBc/totalPriceCal/{id}")
    public LigneBonCommande calculatTotPriceBcL(@PathVariable Long id){
        LigneBonCommande ExistLbc= ligneBonCommandeRep.getOne(id);
        Produit prd=ExistLbc.getProduit();
        BigDecimal unitP= prd.getUnitPrice();
        ExistLbc.setTotalLinePrice(unitP.multiply(ExistLbc.getQuantity()));
        return ligneBonCommandeRep.saveAndFlush(ExistLbc);
    }

    /**
     * this method is used to change quantity of the LignBonCommande
     * @param id
     * @param lbc
     * @return
     */
    @PutMapping("/ligneBc/changeQty/{id}")
    public LigneBonCommande changeQuantity(@PathVariable Long id,@RequestBody LigneBonCommande lbc){
        LigneBonCommande ExistLbc= ligneBonCommandeRep.getOne(id);
        ExistLbc.setQuantity(lbc.getQuantity());
        calculatTotPriceBcL(id);
        return ligneBonCommandeRep.saveAndFlush(ExistLbc);
    }
    @Autowired
    private BonCommandeRepository bonCommandeRepository;
    @PutMapping("/ligneBc/AssignBonC/{idLbc}/{idBc}")
    public LigneBonCommande assignRecette(@PathVariable Long idLbc, @PathVariable Long idBc){
        LigneBonCommande ExistLbc=ligneBonCommandeRep.getOne(idLbc);
        BonCommande AssignBc=bonCommandeRepository.getOne(idBc);
        List<LigneBonCommande> LstLbc=AssignBc.getLigneBonCommande();
        LstLbc.add(ExistLbc);
        AssignBc.setLigneBonCommande(LstLbc);
        ExistLbc.setBonCommande(AssignBc);
        return ligneBonCommandeRep.saveAndFlush(ExistLbc);
    }
    @DeleteMapping("/ligneBC/{id}")
    public void removeLigneBonCommande(@PathVariable Long id){
        ligneBonCommandeRep.deleteById(id);
    }


}
