package com.nahed.pfe.pasteryManagment.controllers.gestionProduction;

import com.fasterxml.jackson.databind.BeanProperty;
import com.nahed.pfe.pasteryManagment.models.modelsProd.LigneRecette;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Produit;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Recette;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.LigneRecetteRepository;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.ProduitRepository;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.RecetteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class RecetteController {
    @Autowired
    private RecetteRepository recetteRepository;

    @GetMapping("/Recettes")
    public List<Recette> retreiveAllRecettes(){
        return recetteRepository.findAll();
    }
    @GetMapping("/Recette/{id}")
    public Recette retreiveRecetteById(@PathVariable Long id){
        return recetteRepository.getOne(id);
    }

    /***
     * To create a Recette we have just to enter the designation
     * the dateCreation is instantly create*
     * @param rc
     * @return
     */
    @PostMapping("/Recette")
    public Recette createRecette(@RequestBody Recette rc){
        List<LigneRecette> lstLinRece= rc.getLigneRecette();
        BigDecimal totaCost=BigDecimal.valueOf(0);
        for(LigneRecette lr: lstLinRece){
            BigDecimal cst= lr.getCoastLine();
            totaCost=totaCost.add(cst);
        }
        rc.setRecetteCoast(totaCost);
        return recetteRepository.saveAndFlush(rc);
    }
    @Autowired
    private LigneRecetteRepository ligneRecetteRepository;

    /***
     * Modifier la designation de La Recette et le nombre de pieces que peut produire cette recette.
     * @param id
     * @param rc
     * @return
     */
    @PutMapping("/Recette/adjDesign/{id}")
    public Recette modifierName(@PathVariable Long id, @RequestBody Recette rc){
        Recette ExistRec=recetteRepository.getOne(id);
        if(rc.getDesignation()!=null){ExistRec.setDesignation(rc.getDesignation());}
        if(rc.getRecettePour()!=null){ExistRec.setRecettePour(rc.getRecettePour());}
        return recetteRepository.saveAndFlush(ExistRec);
    }

    /***
     *
     * calculer le cout de la recette "coastRecette"
     * ne peut etre actionner que lorsque nous assignons les lignes de recettes au Recettes
     * @param id
     * @return
     */
    @PutMapping("/Recette/updatecoast/{id}")
    public Recette calculCoastRecette( @PathVariable Long id){
        Recette ExistRec= recetteRepository.getOne(id);
        List<LigneRecette> lstLinRece= ExistRec.getLigneRecette();
        BigDecimal totaCost=BigDecimal.valueOf(0);
        for(LigneRecette lr: lstLinRece){
           BigDecimal cst= lr.getCoastLine();
           totaCost=totaCost.add(cst);
        }
        ExistRec.setRecetteCoast(totaCost);
        return recetteRepository.saveAndFlush(ExistRec);
    }
    @GetMapping("/Recette/{idr}/relatedLignRecette")
    public List<LigneRecette> showAllLinRecette(@PathVariable Long idr){
        Recette Rc=recetteRepository.getOne(idr);
        List<LigneRecette> lstLr=Rc.getLigneRecette();
        return lstLr;
    }

    @DeleteMapping("/Recette/{id}")
    public void removeRecette(@PathVariable Long id){
        recetteRepository.deleteById(id);
    }
    @Autowired
    private ProduitRepository produitRepository;

    @PutMapping("/Recette/{idRc}/assigproduit/{idprd}")
    public Produit assignRecetteToProduit(@PathVariable Long idprd, @PathVariable Long idRc){
        Produit prd =produitRepository.getOne(idprd);
        Recette rc=recetteRepository.getOne(idRc);
        BigDecimal coutRc=rc.getRecetteCoast();
        System.out.println(coutRc);
        Integer CefRpr=rc.getRecettePour();
        System.out.println(CefRpr);
        BigDecimal couprd= coutRc.divide(BigDecimal.valueOf(CefRpr),3, RoundingMode.HALF_EVEN);
        System.out.println(couprd);
        prd.setProdCoast(couprd);
        BigDecimal diff=prd.getUnitPrice().subtract(prd.getProdCoast());
        Integer marge=(diff.divide(prd.getUnitPrice(),4,RoundingMode.HALF_EVEN)).multiply(BigDecimal.valueOf(100)).intValue();
        prd.setPercentageMargin(marge);
         rc.setProduit(prd);
        recetteRepository.saveAndFlush(rc);
        return  produitRepository.saveAndFlush(prd);

    }


}
