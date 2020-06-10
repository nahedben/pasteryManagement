package com.nahed.pfe.pasteryManagment.controllers.GestionStock;

import com.nahed.pfe.pasteryManagment.models.modelsStock.Famille;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.FamilleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class FamilleController {
    @Autowired
    private FamilleRepository familleRepository;


    @GetMapping("/familles")
    public List<Famille> findAllFamille(){
        return familleRepository.findAll();
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
