package com.nahed.pfe.pasteryManagment.controllers.gestionProduction;

import com.fasterxml.jackson.databind.BeanProperty;
import com.nahed.pfe.pasteryManagment.models.modelsProd.LigneRecette;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Recette;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.LigneRecetteRepository;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.RecetteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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
    @PostMapping("/Recette")
    public Recette createRecette(@RequestBody Recette rc){
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
    @DeleteMapping("/Recette/{id}")
    public void removeRecette(@PathVariable Long id){
        recetteRepository.deleteById(id);
    }

}
