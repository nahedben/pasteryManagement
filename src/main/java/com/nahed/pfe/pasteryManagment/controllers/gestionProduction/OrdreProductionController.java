package com.nahed.pfe.pasteryManagment.controllers.gestionProduction;

import com.nahed.pfe.pasteryManagment.models.modelsProd.OrdreProduction;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Produit;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Recette;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.OrdreProductionRepository;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.ProduitRepository;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.RecetteRepository;
import org.aspectj.weaver.ast.Or;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.LocalDateTime.now;

@RestController
@RequestMapping("/api/v1")
public class OrdreProductionController {
    @Autowired
    private OrdreProductionRepository ordreProductionRepository;

    @GetMapping("/OrdPrd")
    public List<OrdreProduction> retreiveAllOrderProd(){
        return ordreProductionRepository.findAll();
    }

    @GetMapping("/OrdPrd/{id}")
    public OrdreProduction retreiveOrdreById(@PathVariable Long id){
        return ordreProductionRepository.getOne(id);
    }
    @PostMapping("/OrdPrd")
    public OrdreProduction addOrderProd(@RequestBody OrdreProduction po){
        return ordreProductionRepository.saveAndFlush(po);
    }
    @Autowired
    private RecetteRepository recetteRepository;
    @PutMapping("OrdPrd/AssignRecet/{idOp}/{idRecet}")
    public OrdreProduction assignRecetToOp(@PathVariable Long idOp, @PathVariable Long idRecet){
        OrdreProduction ExistOrPr=ordreProductionRepository.getOne(idOp);
        Recette AssignRecet= recetteRepository.getOne(idRecet);

        ExistOrPr.setRecette(AssignRecet);
        return ordreProductionRepository.saveAndFlush(ExistOrPr);
    }

    /***
     * cette Methode nous permet d'actionner le debut de production
     * @param id
     * @return
     */
    @PutMapping("/OrdPrd/StartProd/{id}")
    public OrdreProduction startProd(@PathVariable Long id){
        OrdreProduction Existprd= ordreProductionRepository.getOne(id);
        if(Existprd.isDebutProd()==false){
            LocalDateTime now =LocalDateTime.now();
        Existprd.setDateLancement(now);
        ordreProductionRepository.save(Existprd);
        Existprd.setDebutProd(true);
        }
        return ordreProductionRepository.saveAndFlush(Existprd);
    }

    /***
     * Cette Methode nous permet d'actionner la fin d'un ordre de production et le calcule de tempsPrd
     * ainsi d'entrer la quantite reel sortie production
     * @param id
     * @return
     */
    @PutMapping("OrdPrd/finishProd/{id}")
    public OrdreProduction finishProd(@PathVariable Long id,@RequestBody OrdreProduction op){
        OrdreProduction ExistPrd= ordreProductionRepository.getOne(id);
        LocalDateTime start=ExistPrd.getDateLancement();
        LocalDateTime now=LocalDateTime.now();
        System.out.println("La date de lancement: "+start);
        System.out.println("la date Actuel: "+now);

        Duration period = Duration.between(start,now);
        System.out.println("The difference between stat and now: "+period);
        long diff = period.toMinutes();
        System.out.println("The difference between start and now: "+diff);
        //cette condition nous permet de ne pas permettre l'action de valider une production qu'apres 10min de son lancement

        if(diff>10) {
            if (ExistPrd.isProdFinish() == false) {
                ExistPrd.setSortieProd(now);
                System.out.println("La date Sortie de production est :"+ExistPrd.getSortieProd());
                ExistPrd.setProdFinish(true);
                ExistPrd.setQtSortieProd(op.getQtSortieProd());

//                Duration tpsProd = Duration.between(start,now);
//               long timeSpend= tpsProd.toMinutes();
              ExistPrd.setTempsPrd(diff);
              Recette rc= ExistPrd.getRecette();
              Produit prd= rc.getProduit();
              prd.setQuantity(ExistPrd.getQtSortieProd());
            }
        }
        return ordreProductionRepository.saveAndFlush(ExistPrd);
    }
    @DeleteMapping("OrdPrd/{id}")
    public void removeProductionOrder(@PathVariable Long id){
        ordreProductionRepository.deleteById(id);
    }

}
