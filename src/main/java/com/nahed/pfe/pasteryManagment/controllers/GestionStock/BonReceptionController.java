package com.nahed.pfe.pasteryManagment.controllers.GestionStock;

import com.nahed.pfe.pasteryManagment.models.modelsStock.BonReception;
import com.nahed.pfe.pasteryManagment.models.modelsStock.Fournisseur;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.BonReceptionRepository;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class BonReceptionController {

    @Autowired
    private BonReceptionRepository bonReceptionRepository;

    @GetMapping("/bonreceps")
    public List<BonReception> retreiveAllBonRecp(){
        return bonReceptionRepository.findAll();
    }
    @GetMapping("/bonrecep/{id}")
    public BonReception retreiveBonRecep(@PathVariable Long id){
        return bonReceptionRepository.getOne(id);
    }

    @PostMapping("/bonrecep")
    public BonReception addBonrecep(@RequestBody BonReception bonReception) {
        return bonReceptionRepository.saveAndFlush(bonReception);
    }

    @PutMapping("/bonrecep/{id}")
    public BonReception adjustBonRecept(@RequestBody BonReception br, @PathVariable Long id){
        BonReception brExist= bonReceptionRepository.getOne(id);

       if( br.getNumBonRecep()!=null){brExist.setNumBonRecep(br.getNumBonRecep());}
        if( br.getDateBr()!=null){brExist.setDateBr(br.getDateBr());}
        if( br.getNote()!=null){brExist.setNote(br.getNote());}
        if( br.getMatLivreur()!=null){brExist.setMatLivreur(br.getMatLivreur());}
        if( br.getNomLivreur()!=null){brExist.setNomLivreur(br.getNomLivreur());}
        return bonReceptionRepository.saveAndFlush(brExist);
    }

    /****
     * With this method we are able to have the final result of the MontantTotalTTC\
     * by entring the id of the Bon de reception into the path
     * @param id
     * @return
     */
    @PutMapping("/bonrecep/price/{id}")
    public BonReception calculMontntTotTtc(@PathVariable Long id){

        //here we retreive the existing BonReception
        BonReception Existbr=bonReceptionRepository.getOne(id);

//        set the total amount into the bon de reception, calculate the total ttc and set It
        Fournisseur fr =Existbr.getFournisseur();
        Integer taxfr =fr.getTax();
//        Integer taxfr=10;
        BigDecimal mtHt=Existbr.getMontTotHt();
        BigDecimal qotTax=BigDecimal.valueOf(taxfr).divide(BigDecimal.valueOf(100));
        BigDecimal valtax=mtHt.multiply(qotTax);
        BigDecimal mttc = mtHt.add(valtax);
        System.out.print("la valeur de taxe est: " + valtax);
        Existbr.setMontTotTtc(mttc);
        //save and flush all the modification
        return bonReceptionRepository.saveAndFlush(Existbr);

        }
    @Autowired
    private FournisseurRepository fournisseurRepository;

    //tested and working under "assignFourn" on postMan
    @PutMapping("/bonrecep/four/{idbr}/{idfour}")
    public BonReception assignFour(@PathVariable Long idbr, @PathVariable Long idfour){
        BonReception brExist= bonReceptionRepository.getOne(idbr);
        Fournisseur fr =fournisseurRepository.getOne(idfour);

        //update compte fournisseur
        if(brExist.getMontTotHt()!=null){
        fr.setCompte(fr.getCompte().add(brExist.getMontTotTtc()));}

        //adding brExist to the fournisseur List of BonReception
        List<BonReception> listBr= fr.getBonreceptions();
        listBr.add(brExist);
        //update fournisseur and Bon de reception
        fr.setBonreceptions(listBr);
        brExist.setFournisseur(fr);

        fournisseurRepository.saveAndFlush(fr);
        return bonReceptionRepository.saveAndFlush(brExist);
    }


    @DeleteMapping("/bonrecep/{id}")
    public void removeBR(@PathVariable Long id ){
        bonReceptionRepository.deleteById(id);
    }
}
