package com.nahed.pfe.pasteryManagment.controllers.gestionProduction;

import com.nahed.pfe.pasteryManagment.models.modelsCommon.MatierePremiere;
import com.nahed.pfe.pasteryManagment.models.modelsProd.LigneRecette;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Recette;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.LigneRecetteRepository;
import com.nahed.pfe.pasteryManagment.repositories.MatierePremierRepository;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.RecetteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LigneRecetteController {

    @Autowired
    private LigneRecetteRepository ligneRecetteRepository;

    @GetMapping("/ligneRecette")
    public List<LigneRecette> retreiveAllLigneRec(){
//        List<LigneRecette> LLR= ligneRecetteRepository.findAll();
//
//        for(LigneRecette lr :LLR){
//            System.out.println(lr.getConsoUnit());
//        }
        return ligneRecetteRepository.findAll();
    }
    @GetMapping("/ligneRecette/{id}")
    public LigneRecette retreiveOneLigneRec(@PathVariable Long id){
        return ligneRecetteRepository.getOne(id);
    }

    /***
     * Cette Methode nous permet de retirer tous les ligne appartenant a la meme Recette
     * @param id
     * @return
     */
    @GetMapping("/ligneRecette/recette/{id}")
    public List<LigneRecette> retreiveAllLgnRecFromRecette(@PathVariable Long id){
        return ligneRecetteRepository.findAllByRecetteId(id);
    }

    /**
     * To create a LigneRecette we have to just give the consommation
     * here we divide the consommation by 12 to get the unit Consommation
     * since it is not possible in development to try a Recette for a number less than certain amount by produit
     * @param lRec
     * @return
     */
    @PostMapping("/ligneRecette")
    public LigneRecette addNewLignRec(@RequestBody LigneRecette lRec){
        BigDecimal Cons=lRec.getConsommation();
        lRec.setConsommation(Cons);
        return ligneRecetteRepository.saveAndFlush(lRec);
    }
    @Autowired
    private MatierePremierRepository matierePremierRepository;

    /****
     * Here we assign the MatierePremiere TO LigneRecette
     * and calculate the consoUnit of one produit
     * and by the way calculate the LigneRecette Coast
     * @param idRect
     * @param idMatPrem
     * @return
     */
    @PutMapping("/ligneRecette/assignMatPrem/{idRect}/{idMatPrem}")
    public LigneRecette assignMatPremToLignRect(@PathVariable Long idRect, @PathVariable Long idMatPrem){
        LigneRecette ExistLRect= ligneRecetteRepository.getOne(idRect);
        MatierePremiere AddedMatPrem= matierePremierRepository.getOne(idMatPrem);
       BigDecimal prPrice =AddedMatPrem.getPurchaseUnitPrice();
       BigDecimal cons= ExistLRect.getConsommation();
       if(prPrice!=null){
       ExistLRect.setCoastLine(prPrice.multiply(cons));}
       System.out.println("this is the consommation:" + cons);
       System.out.println("This is th "+prPrice);
       System.out.println("Le cout de la ligne :"+ExistLRect.getCoastLine());
        ExistLRect.setMatierepremier(AddedMatPrem);
//        BigDecimal consUnt = ExistLRect.getConsommation().divide(BigDecimal.valueOf(12),4, RoundingMode.HALF_UP);
//        ExistLRect.setConsoUnit(consUnt);
        return ligneRecetteRepository.saveAndFlush(ExistLRect);
    }
    /***
     *
     * Modifier LigneRecette au niveau de la consommation
     * @param id
     * @param lr
     * @return
     */

    @PutMapping("/LigneRecette/{id}")
    public LigneRecette updateLigneRecette(@PathVariable Long id, @RequestBody LigneRecette lr){
        LigneRecette ExistLr= ligneRecetteRepository.getOne(id);
        if(lr.getConsommation()!=null){
            ExistLr.setConsommation(lr.getConsommation());
//            BigDecimal consUnt = ExistLr.getConsommation().divide(BigDecimal.valueOf(12),4, RoundingMode.HALF_UP);
//            ExistLr.setConsoUnit(consUnt);
        }
        return ligneRecetteRepository.saveAndFlush(ExistLr);
    }
    @DeleteMapping("/LigneRecette/{id}")
    public void removeLigneRecette(@PathVariable Long id){
        ligneRecetteRepository.deleteById(id);
    }

    @Autowired
    private RecetteRepository recetteRepository;

    /***
     * Cette methode nous permet d'assigner la Recette a ligneRecette en utilisant les id de chacune
     * @param idLgnRecet
     * @param idRecette
     * @return
     */
    @PutMapping("/LigneRecette/Recette/{idLgnRecet}/{idRecette}")
    public LigneRecette assignRecetteToLignRct(@PathVariable Long idLgnRecet, @PathVariable Long idRecette ){
        LigneRecette lr =ligneRecetteRepository.getOne(idLgnRecet);
        Recette rc=recetteRepository.getOne(idRecette);
        List<LigneRecette> listLrc=rc.getLigneRecette();
        listLrc.add(lr);
        rc.setLigneRecette(listLrc);
        recetteRepository.saveAndFlush(rc);
        lr.setRecette(rc);
        return ligneRecetteRepository.saveAndFlush(lr);
    }


}
