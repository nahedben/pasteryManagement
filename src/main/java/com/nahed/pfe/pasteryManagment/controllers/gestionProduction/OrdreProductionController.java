package com.nahed.pfe.pasteryManagment.controllers.gestionProduction;

import com.nahed.pfe.pasteryManagment.models.modelsCommon.MatierePremiere;
import com.nahed.pfe.pasteryManagment.models.modelsProd.LigneRecette;
import com.nahed.pfe.pasteryManagment.models.modelsProd.OrdreProduction;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Produit;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Recette;
import com.nahed.pfe.pasteryManagment.models.modelsStock.Depot;
import com.nahed.pfe.pasteryManagment.repositories.MatierePremierRepository;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.LigneRecetteRepository;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.OrdreProductionRepository;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.ProduitRepository;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.RecetteRepository;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.DepotRepository;
import org.aspectj.weaver.ast.Or;
import org.hibernate.criterion.Order;
import org.hibernate.id.BulkInsertionCapableIdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.nahed.pfe.pasteryManagment.models.modelsCommon.ProductUnitEnum.UNIT;
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

    /**
     * We create a ProductionOrder by putting the quantity on the RequestBody
     * @param po
     * @return
     */
    @PostMapping("/OrdPrd")
    public OrdreProduction addOrderProd(@RequestBody OrdreProduction po){
        return ordreProductionRepository.saveAndFlush(po);
    }

    @Autowired
    private RecetteRepository recetteRepository;

    /***
     * Assigner Recette a l'ordre de production
     * @param idOp
     * @param idRecet
     * @return
     */
    @PutMapping("OrdPrd/{idOp}/AssignRecet/{idRecet}")
    public OrdreProduction assignRecetToOp(@PathVariable Long idOp, @PathVariable Long idRecet){
        OrdreProduction ExistOrPr=ordreProductionRepository.getOne(idOp);
        Recette AssignRecet= recetteRepository.getOne(idRecet);

        ExistOrPr.setRecette(AssignRecet);
        return ordreProductionRepository.saveAndFlush(ExistOrPr);
    }
    @Autowired
    private LigneRecetteRepository ligneRecetteRepository;
    @Autowired
    private MatierePremierRepository matierePremierRepository;
    @Autowired
    private DepotRepository depotRepository;
    /**
     * cette mthode nous permet de  mettre a jour le stock de matiere premier si le po est lancer et le stock n'a jamais
     * ete mis a jours.
     * si on valide cette methode on ne pas corriger les donner de nouveau
     *
     * La partie de mise a jour de la chargeActuel de depot n'est pas tester a le tester
     * @param idop
     * @return
     */
    @PutMapping("/OrdPrd/updateStockMat/{idop}")
    public List<MatierePremiere> updateStockMatPrem(@PathVariable Long idop) {
        OrdreProduction op = ordreProductionRepository.getOne(idop);
        BigDecimal qtPrd = op.getQuantity();
        Recette rc = op.getRecette();
        List<LigneRecette> lstLRc = rc.getLigneRecette();
        List<MatierePremiere> lstMtModifier = new ArrayList<>();
        BigDecimal coefRecetteProd = BigDecimal.valueOf(rc.getRecettePour());
        if (op.isDebutProd() == true && op.isStockupdate() == false) {
            for (LigneRecette lr : lstLRc) {
                BigDecimal totalCons = qtPrd.multiply(lr.getConsommation().divide(coefRecetteProd, 4, RoundingMode.HALF_EVEN));
                MatierePremiere mt = lr.getMatierepremier();
                BigDecimal stkmt = mt.getQuantity();
                Depot dp = mt.getDepot();
                BigDecimal chargeDp = dp.getActualCharge();
                    if (mt.getUnit() != UNIT) {
                        mt.setQuantity(stkmt.subtract(totalCons));
                        if (dp.getActualCharge() != null) {
                            dp.setActualCharge(chargeDp.subtract(totalCons));
                        } else {
                            mt.setQuantity(stkmt.subtract(totalCons.setScale(0, RoundingMode.FLOOR)));
                            if (dp.getActualCharge() != null) {
                                dp.setActualCharge(chargeDp.subtract(totalCons.setScale(0, RoundingMode.FLOOR)));
                            }
                        }
                    }
                    depotRepository.saveAndFlush(dp);
                    matierePremierRepository.saveAndFlush(mt);
                    System.out.println(stkmt + " est Le Stock de la matiere premiere" + mt.getName() + " soustraire la consommation de:" + totalCons + " il reste " + stkmt.subtract(totalCons));
                    lstMtModifier.add(mt);
                }
                op.setStockupdate(true);
                ordreProductionRepository.saveAndFlush(op);
            }
            return lstMtModifier;
        }


    /**
     * Cette methode nous permet d'afficher la liste de besoin en matiere premiere pour un OrdreProductin
     * @param idop
     * @return
     */
    @PatchMapping("OrdPrd/Conso/{idop}")
    public List<String> etatConsommation(@PathVariable Long idop){
        OrdreProduction op= ordreProductionRepository.getOne(idop);
        BigDecimal qtPrd=op.getQuantity();
        Recette rc= op.getRecette();
        List<LigneRecette> lstLRc=rc.getLigneRecette();
        List<MatierePremiere> lstMtModifier= new ArrayList<>();
        List<String> lstLignBesoi=new ArrayList<>();
        BigDecimal coefRecetteProd= BigDecimal.valueOf(rc.getRecettePour());
        String ligneBesoin=null;
        for(LigneRecette lr:lstLRc){
            BigDecimal totalCons = qtPrd.multiply(lr.getConsommation().divide(coefRecetteProd, 4, RoundingMode.HALF_EVEN));
            MatierePremiere mt=lr.getMatierepremier();
            BigDecimal stkmt=mt.getQuantity();

            if(mt.getUnit()!=UNIT){
                 ligneBesoin="id: "+mt.getId()+"-> "+String.format(mt.getName())+": "+totalCons+" "+mt.getUnit();
              }else{
                ligneBesoin="id: "+mt.getId()+"-> "+String.format(mt.getName())+": "+totalCons.setScale(0,RoundingMode.FLOOR)+" "+mt.getUnit();
                }
            lstLignBesoi.add(ligneBesoin);
        }
        return lstLignBesoi;
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
     * ainsi d'entrer la quantite reel sortie production et update la quantite en stock
     * @param id
     * @return
     */
    @Autowired
    private ProduitRepository produitRepository;
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
                BigDecimal initQtprd= prd.getQuantity();
              prd.setQuantity(initQtprd.add(ExistPrd.getQtSortieProd()));
              produitRepository.saveAndFlush(prd);
            }
        }
        return ordreProductionRepository.saveAndFlush(ExistPrd);
    }

    /***
     * Cette methode nous permet d'editer la QtySortieProduction
     * ne peut pas etre executer que lorsque c'est un Po dont sa sortie est deja annoncee
     * @param idprd
     * @param op
     * @return
     */
    @PutMapping("OrdPrd/{idprd}/EditerSortieProd")
    public OrdreProduction EditerSortieProduction(@PathVariable Long idprd, @RequestBody OrdreProduction op){
        OrdreProduction ExistOp=ordreProductionRepository.getOne(idprd);
        BigDecimal SortieExi= ExistOp.getQtSortieProd();
        BigDecimal newSortieprd=op.getQtSortieProd();
        Recette rcExistant=ExistOp.getRecette();
        Produit pr=rcExistant.getProduit();
        BigDecimal QtPrdExist= pr.getQuantity();
        BigDecimal QtBefore= QtPrdExist.subtract(SortieExi);
        BigDecimal newQtProd=QtBefore.add(newSortieprd);
        System.out.println("L'ancienne quantite de stock: "+QtBefore);
        System.out.println("La nouvelle quantity de stock: "+newQtProd );
        if(ExistOp.getQtSortieProd()!=null){
        pr.setQuantity(newQtProd);
        produitRepository.saveAndFlush(pr);
        ExistOp.setQtSortieProd(newSortieprd);}
        return ordreProductionRepository.saveAndFlush(ExistOp);
    }
    @PutMapping("OrdPrd/DebutProd/{id}")
    public OrdreProduction editerDebutProd(@PathVariable Long id){
        OrdreProduction op=ordreProductionRepository.getOne(id);
        op.setDebutProd(true);
        return ordreProductionRepository.saveAndFlush(op);
    }
    @DeleteMapping("OrdPrd/{id}")
    public void removeProductionOrder(@PathVariable Long id){
        ordreProductionRepository.deleteById(id);
    }

}
