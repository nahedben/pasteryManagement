package com.nahed.pfe.pasteryManagment.controllers.GestionStock;

import com.nahed.pfe.pasteryManagment.models.modelsStock.Fournisseur;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class FournisseurController {

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @GetMapping("/fourns")
    public List<Fournisseur> showAllFournisseur(){
        return fournisseurRepository.findAll();
    }
    @GetMapping("/fourn/{id}")
    public Fournisseur showFournisseur(@PathVariable Long id){
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
        if(fr.getCompte()==null){frExist.getCompte();}
        if(fr.getEmail()!=null){frExist.setEmail(fr.getEmail());}
        if(fr.getDateCreation()==null){frExist.getDateCreation();}
        if(fr.getPhone()!=null){frExist.setPhone(fr.getPhone());}
        if(fr.getAddress()!=null){frExist.setAddress(fr.getAddress());}
        if(fr.getTax()!=null){frExist.setTax(fr.getTax());}
        return fournisseurRepository.saveAndFlush(frExist);
    }

    @DeleteMapping("/four/{id}")
    public void removeFr(@PathVariable Long id){
        fournisseurRepository.deleteById(id);
    }
}
