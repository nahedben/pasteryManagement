package com.nahed.pfe.pasteryManagment.controllers.GestionStock;

import com.nahed.pfe.pasteryManagment.models.modelsCommon.MatierePremiere;
import com.nahed.pfe.pasteryManagment.models.modelsStock.BonReception;
import com.nahed.pfe.pasteryManagment.models.modelsStock.Depot;
import com.nahed.pfe.pasteryManagment.models.modelsStock.Fournisseur;
import com.nahed.pfe.pasteryManagment.models.modelsStock.LigneBonReception;
import com.nahed.pfe.pasteryManagment.repositories.*;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.BonReceptionRepository;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.DepotRepository;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.FournisseurRepository;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.LigneBonReceptionRepository;
import com.nahed.pfe.pasteryManagment.services.LigneBonReceptionService;
import com.nahed.pfe.pasteryManagment.services.MatierePremiereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LigneBonReceptionController {

    @Autowired
    private LigneBonReceptionRepository ligneBonReceptionRepository;
    @Autowired
    private LigneBonReceptionService ligneBonReceptionService;
    @Autowired
    private MatierePremierRepository matierePremierRepository;
    @Autowired
    private MatierePremiereService matierePremiereService;

        @GetMapping("/LigneBonReceps")
        public List<LigneBonReception> getAllTheBon(){
//        List<MatierePremiere> lmt= matierePremierRepository.findAll();
//        List<LigneBonReception> ltlbr =ligneBonReceptionRepository.findAll();
//        for(LigneBonReception lr : ltlbr){
//            ligneBonReceptionService.automateDescriptionBr(lr,lmt);
//        }
        return ligneBonReceptionRepository.findAll();
    }
        @GetMapping("LigneBonRecep/{id}")
        public LigneBonReception retreiveBR(@PathVariable Long id){
        return ligneBonReceptionRepository.getOne(id);
        }

    /**
     * Creation d'une ligne de bon de commande en utilisant quantity
     * in this method I have updated the both boolean attributes to false
     * @param lbr
     * @return
     */
    @PostMapping("/LigneBonRecep")
        public LigneBonReception addRecetLBRecep(@RequestBody LigneBonReception lbr){
        lbr.setTotalAffected(false);
        lbr.setQtyInserted(false);
        return ligneBonReceptionRepository.saveAndFlush(lbr);
        }

    /***
     * Dans le cas d'une modification de la quantity d'une ligneBonReception nous allons utiliser cette methode
     *qui demande en entre la nouvelle quantite et l'id ligneBonReception
     *
     *
     * A Ce stade on a un probleme au niveau du montant total HT lorsqu'on mis 1000 litre
     * ainsi on n'a pas pus faire l'update du total TTC au niveau de bon de reception
     * @param lbr
     * @param id
     * @return
     */
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private FournisseurRepository fournisseurRepository;

        @PutMapping("/LigneBonRecep/{id}")
        public LigneBonReception adjustLigneBR(@RequestBody LigneBonReception lbr,@PathVariable Long id){
            LigneBonReception lbrExist= ligneBonReceptionRepository.getOne(id);
            MatierePremiere mp = lbrExist.getMatierepremier();
            Depot dp=mp.getDepot();
            BonReception ExistBr =lbrExist.getBonReception();

            System.out.println("QtyStock: "+mp.getQuantity()+" / "+ "Qty inserrer en ligne"+lbr.getQuantity()+"qtity " +
                    "depot"+dp.getActualCharge());

            //recuperer la quantite dans la base
            BigDecimal mpQ =mp.getQuantity();
            BigDecimal lbrExistQ=lbrExist.getQuantity();
            BigDecimal dpQt=dp.getActualCharge();
            BigDecimal ExistMtTotHT =ExistBr.getMontTotHt();
            BigDecimal ExistMtTotTtc=ExistBr.getMontTotTtc();
            //recuperer la nouvelle entree
            BigDecimal newQt=lbr.getQuantity();
            //calculer le nouveau total de ligneBonReception
            BigDecimal totalprice = newQt.multiply(mp.getPurchaseUnitPrice());
            //La difference entre l'ancien enregistrement et le nouveau au niveau de la quantite
            BigDecimal diff =lbrExistQ.subtract(newQt);
            //calculer la valeur de stock apres ajout
            BigDecimal finalQt=mpQ.subtract(diff);
            //Calcule de la charge de depot actuel:
//            BigDecimal variation = finalQt.subtract(mpQ);
            BigDecimal nouvMpQt=dpQt.subtract(diff);
            //Calculer la valeur de MontnantTotalHt
            BigDecimal DifTotPRice= totalprice.subtract(lbrExist.getTotal());
            System.out.println("la difference entre l'existant et le resultat apres la nouvelle quantity: "+DifTotPRice);
            BigDecimal MtHTax= ExistMtTotHT.subtract(DifTotPRice);
            System.out.println("Le nouveau montant total  hors taxe est : "+MtHTax);
            Fournisseur ExistFr= ExistBr.getFournisseur();
           BigDecimal coefTax= (BigDecimal.valueOf(ExistFr.getTax()).multiply(BigDecimal.valueOf(0.01))).add(BigDecimal.valueOf(1));
            System.out.println("La valeur de tax est "+ExistFr.getTax());
            System.out.println("Le Coef de taxe est :" +coefTax);
            BigDecimal MtTtc=MtHTax.multiply(coefTax);
            System.out.println("Le total TTC est :"+MtTtc);
            BigDecimal ExistCompte=ExistFr.getCompte();
            BigDecimal DiffMtTTC= ExistBr.getMontTotHt().subtract(MtTtc);
            BigDecimal newCompte= ExistCompte.subtract(DiffMtTTC);

            //affecter les nouvelles valeurs a leurs attribut
            lbrExist.setQuantity(newQt);
            lbrExist.setTotal(totalprice);
            ExistBr.setMontTotHt(MtHTax);
            ExistBr.setMontTotTtc(MtTtc);
            mp.setQuantity(finalQt);
            dp.setActualCharge(nouvMpQt);
            mp.setDepot(dp);
            ExistFr.setCompte(newCompte);

            lbrExist.setMatierepremier(mp);

            fournisseurRepository.save(ExistFr);
            bonReceptionRepository.save(ExistBr);
            depotRepository.saveAndFlush(dp);
            matierePremierRepository.saveAndFlush(mp);
            return ligneBonReceptionRepository.saveAndFlush(lbrExist);

    }

    /**
     * cette methode nous permet d'assigner ligneBonReception a la matiere premiere en question
     * en entre a la path l'id de matierePremiere et ensuite l'id BonReception
     * le stock de la matiere en question sera updated ainsi que le ActualCharge de depot.
     * calculer le totalPrice de ligneBonReception
     * @param matPremId
     * @param ligneBonRecepId
     * @return
     */
    @PutMapping("lignBr/assignMatPrem/{matPremId}/{ligneBonRecepId}")
    public LigneBonReception assignMatPrem(@PathVariable Long matPremId ,@PathVariable Long ligneBonRecepId){
        //lbr exemple id: 1 , quantity : 15 , matprem :null
        LigneBonReception  lbr = ligneBonReceptionRepository.getOne(ligneBonRecepId);
        MatierePremiere mp = matierePremierRepository.getOne(matPremId);//mp quantity 9dima
        Depot dp=mp.getDepot();
        BigDecimal dpQt =dp.getActualCharge();

        // update MP quantity
        if(lbr.isQtyInserted()==false){
            System.out.println("lavaleur de Quantity inserted is:"+lbr.isQtyInserted());
            if(mp.getQuantity()==null){
            BigDecimal v=new BigDecimal("0");
            mp.setQuantity(v);}

            mp.setQuantity((mp.getQuantity().add((lbr.getQuantity()))));
            dp.setActualCharge(dpQt.add(lbr.getQuantity()));

        //calculate the totalprice
            lbr.setTotal(mp.getPurchaseUnitPrice().multiply(lbr.getQuantity()));
            //assign LBR to MP
            List<LigneBonReception> listBonRecep =  mp.getLigneBonReceptions();
            listBonRecep.add(lbr);
            mp.setLigneBonReceptions((listBonRecep));
            matierePremierRepository.save(mp);
            lbr.setMatierepremier(mp);
            lbr.setQtyInserted(true);
            ligneBonReceptionRepository.save(lbr);
        }else{
            System.out.println("you can't assign again");
        }
        matierePremierRepository.saveAndFlush(mp);
        return ligneBonReceptionRepository.saveAndFlush(lbr);
    }
    @Autowired
    private BonReceptionRepository bonReceptionRepository;

    /***
     * Methode qui nous permet d'assigner Ligne BonReception au BonReception
     * @param idlbr
     * @param idbr
     * @return
     */
    @PutMapping("/lignebonrecep/assignBonRecep/{idlbr}/{idbr}")
    public LigneBonReception assiBonRecep(@PathVariable Long idlbr,@PathVariable Long idbr){
        LigneBonReception Existlbr =ligneBonReceptionRepository.getOne(idlbr);
        BonReception assibr =bonReceptionRepository.getOne(idbr);
        //adding the amount of the total price to Bon Ligne de reception to Bon Reception
        if(Existlbr.isTotalAffected()==false){

            if(assibr.getMontTotHt()==null){
                BigDecimal startV=new BigDecimal("0");
                assibr.setMontTotHt(startV.add(Existlbr.getTotal()));
            }


                assibr.setMontTotHt(assibr.getMontTotHt().add(Existlbr.getTotal()));
                Existlbr.setTotalAffected(true);
                bonReceptionRepository.save(assibr);
                ligneBonReceptionRepository.save(Existlbr);
                } else{
            System.out.println(Existlbr.getTotal()+" is Already assigned you aren't allowed to do it again");
                  //  assibr.setMontTotHt(assibr.getMontTotTtc());
        }
        List<LigneBonReception> ltLignbr =assibr.getLignebonreceptions();
        ltLignbr.add(Existlbr);

        assibr.setLignebonreceptions(ltLignbr);
        Existlbr.setBonReception(assibr);
        bonReceptionRepository.saveAndFlush(assibr);
        return ligneBonReceptionRepository.saveAndFlush(Existlbr);
    }
    @DeleteMapping("/ligneBonRecep/{id}")
    public void removeLigne(@PathVariable Long id){
        ligneBonReceptionRepository.deleteById(id);
    }
}
