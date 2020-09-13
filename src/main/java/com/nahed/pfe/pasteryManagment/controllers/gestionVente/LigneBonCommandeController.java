package com.nahed.pfe.pasteryManagment.controllers.gestionVente;

import com.nahed.pfe.pasteryManagment.models.modelVente.BonCommande;
import com.nahed.pfe.pasteryManagment.models.modelVente.LigneBonCommande;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Produit;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.ProduitRepository;
import com.nahed.pfe.pasteryManagment.repositories.venteRepositories.BonCommandeRepository;
import com.nahed.pfe.pasteryManagment.repositories.venteRepositories.LigneBonCommandeRepository;
import org.hibernate.sql.AliasGenerator;
import org.hibernate.validator.internal.xml.CloseIgnoringInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class LigneBonCommandeController {
    @Autowired
    private LigneBonCommandeRepository ligneBonCommandeRep;

    @GetMapping("/ligneBCs")
    public List<LigneBonCommande> retreiveAllLgBc(){
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

    @PostMapping("/ligneBC/prodBCID/{idprd}/{idBc}")
    public LigneBonCommande createLignBCComp(@RequestBody LigneBonCommande lbc, @PathVariable Long idprd, @PathVariable Long idBc){
        LigneBonCommande LbcCr=ligneBonCommandeRep.saveAndFlush(lbc);
       LigneBonCommande lbprdAss= assignProduitToLbc(LbcCr.getId(),idprd);
       LigneBonCommande  lbcprictotal= calculatTotPriceBcL(lbprdAss.getId());
       LigneBonCommande lbcFinal = assignBonC(lbcprictotal.getId(),idBc);
       return lbcFinal;
    }

    @Autowired
    private ProduitRepository produitRepository;

    /***
     * Assign produit to ligneBonCommande
     * @param idlbc
     * @param idproduit
     * @return
     */
    @PutMapping("/ligneBc/{idlbc}/AssignProduit/{idproduit}")
    public LigneBonCommande assignProduitToLbc(@PathVariable Long idlbc,@PathVariable Long idproduit){
        LigneBonCommande ExistLbc =ligneBonCommandeRep.getOne(idlbc);
        Produit AssignProd=produitRepository.getOne(idproduit);
        ExistLbc.setProduit(AssignProd);
        return ligneBonCommandeRep.saveAndFlush(ExistLbc);
    }

    /***
     * methode de calculer de totalPrice de ligne bon de commande
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
     * this method is used to edit quantity of the LignBonCommande
     * @param id
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

    /***
     * Assigne bonCommande to ligneBonCommande
     * @param idLbc
     * @param idBc
     * @return
     */
    @PutMapping("/ligneBc/{idLbc}/AssignBonC/{idBc}")
    public LigneBonCommande assignBonC(@PathVariable Long idLbc, @PathVariable Long idBc) {
        LigneBonCommande ExistLbc = ligneBonCommandeRep.getOne(idLbc);
        BonCommande AssignBc = bonCommandeRepository.getOne(idBc);
        List<LigneBonCommande> LstLbc = new ArrayList<LigneBonCommande>();
              LstLbc =  AssignBc.getLigneBonCommande();
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
