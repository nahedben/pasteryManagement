package com.nahed.pfe.pasteryManagment.controllers.GestionStock;

import com.nahed.pfe.pasteryManagment.models.modelsCommon.MatierePremiere;
import com.nahed.pfe.pasteryManagment.models.modelsStock.BonReception;
import com.nahed.pfe.pasteryManagment.models.modelsStock.Fournisseur;
import com.nahed.pfe.pasteryManagment.models.modelsStock.LigneBonReception;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.BonReceptionRepository;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.FournisseurRepository;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.LigneBonReceptionRepository;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.MatierePremierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@CrossOrigin
@RestController
@RequestMapping("api/v1")
public class FournisseurController {

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @GetMapping("/fourns")
    public List<Fournisseur> retreiveAllFournisseur(){
        return fournisseurRepository.findAll();
    }
    @GetMapping("/fourn/{id}")
    public Fournisseur retrieveOneFournisseur(@PathVariable Long id){
        return fournisseurRepository.getOne(id);
    }
    @PostMapping("/fourn")
    public Fournisseur addFournisseur(@RequestBody Fournisseur fr){
        return fournisseurRepository.saveAndFlush(fr);
    }
    @PutMapping("four/{id}")
    public Fournisseur adjustFournisseur(@RequestBody Fournisseur fr, @PathVariable Long id){
//        Fournisseur frExist =fournisseurRepository.getOne(id);
//        BeanUtils.copyProperties(fr,frExist,"id");
//        return fournisseurRepository.saveAndFlush(frExist);
        Fournisseur frExist =fournisseurRepository.getOne(id);
        if(fr. getName()!=null){frExist.setName(fr.getName());}
        if(fr.getNote()!=null){frExist.setNote(fr.getNote());}
        if(fr.getCompte()!=null){frExist.getCompte();}
        if(fr.getEmail()!=null){frExist.setEmail(fr.getEmail());}
        if(fr.getDateCreation()!=null){frExist.getDateCreation();}
        if(fr.getPhone()!=null){frExist.setPhone(fr.getPhone());}
        if(fr.getAddress()!=null){frExist.setAddress(fr.getAddress());}
        if(fr.getTax()!=null){frExist.setTax(fr.getTax());}

        return fournisseurRepository.saveAndFlush(frExist);
    }
    @Autowired
            private MatierePremierRepository matierePremierRepository;
    @Autowired
            private BonReceptionRepository bonReceptionRepository;
    @Autowired
            private LigneBonReceptionRepository ligneBonReceptionRepository;
    //cette methode nous permet d'afficher les matieres premiere d'un fournisseur
    @GetMapping("mat/four/{id}")
        public Set<MatierePremiere> retreiveAllMatFromFourn(@PathVariable Long id){
//        MatierePremiere mt=matierePremierRepository.getOne(id);
        Set<MatierePremiere> Stmt = new HashSet<MatierePremiere>();
        Fournisseur fr =fournisseurRepository.getOne(id);
        List<BonReception> lsBr= bonReceptionRepository.findAllByFournisseurId(id);
        for(BonReception br: lsBr){
            Long idbr=br.getId();
            List<LigneBonReception> lstlgBr=ligneBonReceptionRepository.findAllByBonReceptionId(idbr);
            for(LigneBonReception lbr: lstlgBr ){
                Long idlbr=lbr.getId();
                MatierePremiere mt=lbr.getMatierepremier();
               // MatierePremiere mt = matierePremierRepository.findByLigneBonReceptionsId(idlbr);
                Stmt.add(mt);
            }
        }
        return Stmt;
        }

        @GetMapping("Bon/four/{id}")
        public List<BonReception> retreiveAllBrFromFournisseur(@PathVariable Long id){
//        Set<BonReception> stBr =new HashSet<BonReception>();
//        Fournisseur fr =fournisseurRepository.getOne(id);
        return bonReceptionRepository.findAllByFournisseurId(id);
        }


    @DeleteMapping("/four/{id}")
    public void removeFr(@PathVariable Long id){
        fournisseurRepository.deleteById(id);
    }
}
