package com.nahed.pfe.pasteryManagment.controllers.gestionVente;

import com.nahed.pfe.pasteryManagment.models.modelVente.Client;
import com.nahed.pfe.pasteryManagment.models.modelVente.Facture;
import com.nahed.pfe.pasteryManagment.models.modelVente.LigneFacture;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Produit;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.ProduitRepository;
import com.nahed.pfe.pasteryManagment.repositories.venteRepositories.ClientRepository;
import com.nahed.pfe.pasteryManagment.repositories.venteRepositories.FactureRepository;
import com.nahed.pfe.pasteryManagment.repositories.venteRepositories.LigneFactureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.Format;
import java.time.LocalDateTime;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class FactureController {
    @Autowired
    private FactureRepository factureRepository;

    /**
     * CreateFacture by inserting typePayement and the date is automated
     * @param fct
     * @return
     */
    @Autowired
    private LigneFactureRepository ligneFactureRepository;

//    @GetMapping("facture/ligmes/idfct")
//    public List<LigneFacture> retreiveLgnFctFccture(@PathVariable Long idfct){
//        return ligneFactureRepository.findAllByFactureId(idfct);
//    }
    @PostMapping("/facture")
    public Facture createFacture(@RequestBody Facture fct){
        return factureRepository.saveAndFlush(fct);
    }
    @GetMapping("/factures")
    public List<Facture> retreiveAllFacture(){
        return factureRepository.findAll();
    }

    @GetMapping("/facture/{id}")
    public Facture retreiveFactureById(@PathVariable Long id){
        return factureRepository.getOne(id);
    }
    @Autowired
    private ClientRepository clientRepository;

    @PutMapping("/facture/{idfct}/assignClient/{idcl}")
    public Facture assignClientToFacture(@PathVariable Long idfct, @PathVariable Long idcl){
        Facture fc=factureRepository.getOne(idfct);
        Client cl=clientRepository.getOne(idcl);
        fc.setClient(cl);
        return factureRepository.saveAndFlush(fc);
    }

    /***
     * CalculeFactureTotalPrice
     * @param id
     * @return
     */
    @PutMapping("facture/{id}/TotalPrice")
    public Facture calculeFactureTotalpay(@PathVariable Long id){
        Facture fc=factureRepository.getOne(id);
        List<LigneFacture> lstLfct=fc.getLigneFacture();
        BigDecimal totalFact=new BigDecimal("0");
        BigDecimal totLign;
        for(LigneFacture lf:lstLfct){
            totLign=lf.getTotalPrice();
            totalFact=totalFact.add(totLign);

            }
        System.out.println("Le total: "+totalFact);
        fc.setTotalPriceTtc(totalFact);
        return factureRepository.saveAndFlush(fc);
    }
//    @Autowired
//    private LigneFactureRepository ligneFactureRepository;
    @Autowired
    private ProduitRepository produitRepository;

    /**
     * methode de validation de facture et mise a jour de stock produit
     * @param id
     * @return
     */
    @PutMapping("facture/{id}/validation")
    public Facture validateFacture(@PathVariable Long id){
        Facture fc=factureRepository.getOne(id);
        List<LigneFacture> lstLf=fc.getLigneFacture();
        if(fc.isFactvalidation()==false){
            for(LigneFacture lf:lstLf) {
                Produit pr = lf.getProduit();
                System.out.println("Le produit est:"+pr.getName());
                BigDecimal existQt = pr.getQuantity();
                BigDecimal Qtvendu = lf.getQuantity();
                BigDecimal newQt=new BigDecimal("0");
                newQt= existQt.subtract(Qtvendu);
                System.out.println("La nouvelle quantite de stock"+newQt);
                pr.setQuantity(newQt);

                System.out.println("Le produit: "+pr.getName()+"qtExistant: "+existQt+" La quantite vendu: "+ Qtvendu+ " le nouveau stock: "+newQt);
                produitRepository.saveAndFlush(pr);
            }
            System.out.println(" La facture est deja valide");
        }
        fc.setFactvalidation(true);
        return factureRepository.saveAndFlush(fc);
    }
    @PutMapping("facture/{id}/reglement")
    public Facture ActionnerReglement(@PathVariable Long id){
        Facture fc=factureRepository.getOne(id);
       Client cl= fc.getClient();
       BigDecimal Montvente=fc.getTotalPriceTtc();
       System.out.println("Le montant de la facture: "+Montvente);
       BigDecimal chiffAchaCl= cl.getChiffreAchat();
       if(fc.isRegelement()==false){
           if(chiffAchaCl==null){
                   BigDecimal intiChiffre=new BigDecimal("0");
               cl.setChiffreAchat(intiChiffre.add(Montvente));
           }else{
           cl.setChiffreAchat(chiffAchaCl.add(Montvente));
                }
           cl.setLastVisit(LocalDateTime.now());



               clientRepository.saveAndFlush(cl);
       }
        fc.setRegelement(true);
           return factureRepository.saveAndFlush(fc);
    }

    @PatchMapping("/facture/{idfc}/annulerlignFc/{LinFID}")
    public Facture annulerLigneFctFromFacture(@PathVariable Long idfc, @PathVariable Long LinFID){
        Facture fc= factureRepository.getOne(idfc);
        LigneFacture Lf= ligneFactureRepository.getOne(LinFID);
        List<LigneFacture> lstLgf=fc.getLigneFacture();

       Produit prd=Lf.getProduit();
       BigDecimal qtyInit=prd.getQuantity();
       System.out.println("La quantite initial est :"+qtyInit);
       BigDecimal QttoRem=Lf.getQuantity();
       System.out.println("La quatite dans ligne de Facture est :"+QttoRem);

       BigDecimal newQt=qtyInit.add(QttoRem);
       System.out.println("La nouvelle quantite: "+newQt);
       System.out.println("La Id de produit: "+prd.getId());
       //modifier la quantite de produit dans la base de donner
       prd.setQuantity(newQt);
       produitRepository.saveAndFlush(prd);

        BigDecimal montantLf=Lf.getTotalPrice();
        BigDecimal montantFc=fc.getTotalPriceTtc();
        BigDecimal newmMontantFc=montantFc.subtract(montantLf);
        System.out.println("le montant dans la ligne de facture est: "+montantLf);
        System.out.println("le montantTTc de facture est: "+montantFc);
        System.out.println("le nouveau montantTTc de facture est: "+newmMontantFc);
        //modifier le TotalPriceTTc dans la base
        fc.setTotalPriceTtc(newmMontantFc);
        factureRepository.saveAndFlush(fc);

        //Modifier le chiffreAchat Client
        Client cl=fc.getClient();
        BigDecimal ChiffInit=cl.getChiffreAchat();
        BigDecimal newChiffre= ChiffInit.subtract(montantLf);
        System.out.println("LE chiffre d'achat initial est : "+ ChiffInit);
        System.out.println("LE nouveau chiffre d'achat est : "+ newChiffre);

        //modifier le chiffreAchat CLient
        cl.setChiffreAchat(newChiffre);
        clientRepository.saveAndFlush(cl);
        //dans le cas ou nous avons un client qui doit etre taxe avec un tax
        if(cl.getTax()!=null){
            BigDecimal tax=cl.getTax();
            BigDecimal TotalPriceUpdated=fc.getTotalPriceTtc();
            BigDecimal newTaxValue= TotalPriceUpdated.multiply(tax);
            System.out.println("L'ancienne valeur de tax est : "+fc.getTaxValue());
            System.out.println("La nouvelle valeur de tax est  : "+newTaxValue);
            //fc.setTaxValue(newTaxValue);
        }else{System.out.println("La valeur de tax est nulle");}

        ligneFactureRepository.deleteById(LinFID);
        return factureRepository.saveAndFlush(fc);

    }

    @PatchMapping("/facture/{idfc}/editerlignFc/{LinFID}")
    public Facture editerLigneFctFromFacture(@PathVariable Long idfc, @PathVariable Long LinFID,@RequestBody LigneFacture lf){
        Facture fc= factureRepository.getOne(idfc);
        LigneFacture ExistLf= ligneFactureRepository.getOne(LinFID);
        List<LigneFacture> lstLgf=fc.getLigneFacture();

        Produit prd=ExistLf.getProduit();
        BigDecimal qtyInit=prd.getQuantity();
        System.out.println("La quantite initial est :"+qtyInit);
        BigDecimal QttoRem=ExistLf.getQuantity();
        System.out.println("La quatite dans ligne de Facture est :"+QttoRem);
        BigDecimal editedQty=lf.getQuantity();
        System.out.println("La quatite modifier dans ligne de Facture est :"+editedQty);
        BigDecimal newQt=qtyInit.add(QttoRem).subtract(editedQty);
        System.out.println("La nouvelle quantite: "+newQt);
        System.out.println("La Id de produit: "+prd.getId());
        //modifier la quantite de produit dans la base de donner
        prd.setQuantity(newQt);
        produitRepository.saveAndFlush(prd);

        BigDecimal newMtLf=lf.getQuantity().multiply(prd.getUnitPrice());
        BigDecimal montantLf=ExistLf.getTotalPrice();
        BigDecimal montantFc=fc.getTotalPriceTtc();
        BigDecimal newmMontantFc=montantFc.subtract(montantLf).add(newMtLf);
        System.out.println("le montant dans la ligne de facture est: "+montantLf);
        System.out.println("le nouoveau montant dans la ligne de facture sera: "+newMtLf);

        System.out.println("le montantTTc de facture est: "+montantFc);
        System.out.println("le nouveau montantTTc de facture est: "+newmMontantFc);

        //modifier le TotalPriceTTc dans la base
        fc.setTotalPriceTtc(newmMontantFc);
        factureRepository.saveAndFlush(fc);
        ExistLf.setTotalPrice(newmMontantFc);
        ExistLf.setQuantity(editedQty);

        //Modifier le chiffreAchat Client
        Client cl=fc.getClient();
        BigDecimal ChiffInit=cl.getChiffreAchat();
        BigDecimal newChiffre= ChiffInit.subtract(montantLf).add(newMtLf);
        System.out.println("LE chiffre d'achat initial est : "+ ChiffInit);
        System.out.println("LE nouveau chiffre d'achat est : "+ newChiffre);

        //modifier le chiffreAchat CLient
        cl.setChiffreAchat(newChiffre);
        clientRepository.saveAndFlush(cl);
        //dans le cas ou nous avons un client qui doit etre taxe avec un tax
        if(cl.getTax()!=null){
            BigDecimal tax=cl.getTax();
            BigDecimal TotalPriceUpdated=fc.getTotalPriceTtc();
            BigDecimal newTaxValue= TotalPriceUpdated.multiply(tax);
            System.out.println("L'ancienne valeur de tax est : "+fc.getTaxValue());
            System.out.println("La nouvelle valeur de tax est  : "+newTaxValue);
            //fc.setTaxValue(newTaxValue);
        }else{System.out.println("La valeur de tax est nulle");}

        ExistLf.setQuantity(lf.getQuantity());
        ExistLf.setTotalPrice(newMtLf);
        ligneFactureRepository.saveAndFlush(ExistLf);
        return factureRepository.saveAndFlush(fc);
    }
    @DeleteMapping("/facture/{id}")
    public void removeFacture(@PathVariable Long id){
        factureRepository.deleteById(id);
    }

    @PutMapping("/facture/taxvalue/{id}")
    public Facture claculTaxValue(@PathVariable Long id){
        Facture fc= factureRepository.getOne(id);
        Client cl=fc.getClient();
        if(cl.getTax()!=null) {
            BigDecimal Tax = cl.getTax();
            BigDecimal Taxval = Tax.multiply(fc.getTotalPriceTtc());
            fc.setTaxValue(Taxval);
        }
            return factureRepository.saveAndFlush(fc);
    }

}
