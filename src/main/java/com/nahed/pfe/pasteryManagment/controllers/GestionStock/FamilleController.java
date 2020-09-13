package com.nahed.pfe.pasteryManagment.controllers.GestionStock;

import com.nahed.pfe.pasteryManagment.models.modelsProd.Produit;
import com.nahed.pfe.pasteryManagment.models.modelsStock.Famille;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.ProduitRepository;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.FamilleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.aspectj.runtime.internal.Conversions.longValue;

@CrossOrigin
@RestController
@RequestMapping("api/v1")
public class FamilleController {
    @Autowired
    private FamilleRepository familleRepository;
    @Autowired
    private ProduitRepository produitRepository;


    @GetMapping("/fam/produit/{id}")
    public List<Produit> retreiveAllProduitFam(@PathVariable Long id){
        Famille fam = familleRepository.getOne(id);
        List<Produit> lstPrd = fam.getProduits();
        return lstPrd;
    }
    @GetMapping("/fam/Nbrproduit/{id}")
    public int retreiveNbrProduitFam(@PathVariable Long id){
       List<Produit> lprd= retreiveAllProduitFam(id);
       int cont = lprd.size();
       return cont;
    }
    @GetMapping("/familles")
    public List<Famille> findAllFamille(){

        List<Famille>lst=familleRepository.findAll();
        for(Famille fam: lst){
            Long id = fam.getId();
            List<Produit> lprd= retreiveAllProduitFam(id);
            Long cont = longValue(lprd.size());
            fam.setNbrProd(cont);
        }
        return lst;
    }

    @GetMapping("/famille/{id}")
    public Famille findFamilleWithID(@PathVariable Long id){
        return familleRepository.getOne(id);
    }

    @PostMapping("/famille")
    public Famille addNewFamill(@RequestBody Famille famille){
        return familleRepository.saveAndFlush(famille);
    }

    @PutMapping("/famille/{id}")
    public Famille editFamille(@RequestBody Famille famille, @PathVariable Long id){
        Famille existfm= familleRepository.getOne(id);
        BeanUtils.copyProperties(famille,existfm,"id");
        return familleRepository.saveAndFlush(existfm);
    }

    @DeleteMapping("/famille/{id}")
    public void removeFamille(@PathVariable Long id){
        familleRepository.deleteById(id);

    }
}
