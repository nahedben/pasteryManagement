package com.nahed.pfe.pasteryManagment.controllers.gestionProduction;


import com.nahed.pfe.pasteryManagment.models.modelsProd.OrdreProduction;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Produit;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Recette;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.OrdreProductionRepository;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.ProduitRepository;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.RecetteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProduitController {
    @Autowired
    private ProduitRepository produitRepository;

    @PostMapping("/produit")
    public Produit createProduit(@RequestBody Produit prod){
        return produitRepository.saveAndFlush(prod);
    }
    @GetMapping("/produits")
    public List<Produit> retreiveAllProduit(){
        return produitRepository.findAll();
    }
    @GetMapping("/produit/{id}")
    public Produit retreiveProduit(@PathVariable Long id){
        return produitRepository.getOne(id);
    }

    /***
     * Cette Methode nous permet de faire la modification des attributs
     * name, unit, unitPrice,stockLimit,photo,de l'instance Produit creee
     * @param id
     * @param prd
     * @return
     */
    @PutMapping("produit/{id}")
    public Produit adjustProduit(@PathVariable Long id, @RequestBody Produit prd){
        Produit ExistPrd=produitRepository.getOne(id);
        if(prd.getName()!=null){ExistPrd.setName(prd.getName());}
        if(prd.getUnit()!=null){ExistPrd.setUnit(prd.getUnit());}
        if(prd.getUnitPrice()!=null){ExistPrd.setUnitPrice(prd.getUnitPrice());}
        if(prd.getStockLimit()!=null){ExistPrd.setStockLimit((prd.getStockLimit()));}
        if(prd.getPhoto()!=null){ExistPrd.setPhoto(prd.getPhoto());}
        return produitRepository.saveAndFlush(ExistPrd);
    }
    @Autowired
    private OrdreProductionRepository ordreProductionRepository;

    /**
     * Cette Methode nous permet d'ajouter la quantity d'un production au stock produit a partir de l'id de produit
     * et de l'id de Ordre de production
     * @param idprd
     * @param idOp
     * @return
     */
    @PutMapping("/produit/qt/{idprd}/{idOp}")
    public Produit calculProduit(@PathVariable Long idprd,@PathVariable Long idOp){
        Produit ExistPrd=produitRepository.getOne(idprd);
        Recette rc=ExistPrd.getRecette();
        OrdreProduction po=ordreProductionRepository.getOne(idOp);
        BigDecimal QteProduite= po.getQuantity();
        BigDecimal Stock=ExistPrd.getQuantity();
        if(po.isProdFinish()==true){
            Stock=Stock.add(QteProduite);
            ExistPrd.setQuantity(Stock);
        }

        return produitRepository.saveAndFlush(ExistPrd);
    }

    /**
     * cette methode nous permet de modifer la quantite de produit lors d'un inventaire reel
     * il faut l'appliquer avec autorisation
     *
     */
    @PutMapping("produit/qt/{idprd}")
    public Produit editeQt(@PathVariable Long idprd, @RequestBody Produit prd){
        Produit Existprd= produitRepository.getOne(idprd);
        Existprd.setQuantity(prd.getQuantity());
        return produitRepository.saveAndFlush(Existprd);
    }
//    @PutMapping("/produit/{idprd}/assigRecet/{idRc}")
//    public Produit assignRecetteToProduit(@PathVariable Long idprd, @PathVariable Long idRc){
//        Produit prd =produitRepository.getOne(idprd);
//        Recette rc=recetteRepository.getOne(idRc);
//        prd.setRecette(rc);
//        return produitRepository.saveAndFlush(prd);
//    }

    @Autowired
    private RecetteRepository recetteRepository;

    /** calcule de cout de Produit a partir de cout de la recette
     *
     * @param idr
     * @return
     */
    @PutMapping("produitC/PrdCoast/{idr}")
    public Produit calculProduitCout( @PathVariable Long idr){
        //Produit ExistProd= produitRepository.getOne(id);
       // System.out.println("ExistPRod"+ExistProd);
//        Recette relatedRc=recetteRepository.getOne(idr);
//        System.out.println("RelatedRecette" + relatedRc);
//         Produit ExistProd =relatedRc.getProduit();
//        System.out.println("ExistitngProd: "+ ExistProd);
        Produit ExistPr=produitRepository.getOne(idr);
        System.out.println(ExistPr);



//        BigDecimal totalCofCoast= BigDecimal.valueOf(0);
//        BigDecimal coutRecette =relatedRc.getRecetteCoast();
//        System.out.println("cout de la recette relier au produit"+coutRecette);
//        Integer nbrPrd=relatedRc.getRecettePour();
//        System.out.println("Recette pour "+nbrPrd+ "piece");
//        totalCofCoast=coutRecette.divide(BigDecimal.valueOf(nbrPrd));
//        System.out.println("cout de la recette relier au produit"+coutRecette);
//        ExistProd.setProdCoast(totalCofCoast);
//        //calculer la Percentage margin
//        BigDecimal diff=ExistProd.getUnitPrice().subtract(ExistProd.getProdCoast());
//        Integer marge=(diff.divide(ExistProd.getUnitPrice())).multiply(BigDecimal.valueOf(100)).intValue();
//        ExistProd.setPercentageMargin(marge);
        return produitRepository.saveAndFlush(ExistPr);
    }
//    @Autowired
//    private RecetteRepository recetteRepository;

//    @Autowired
//    private CoefRecProdRepository coefRecProdRepository;
//    @PutMapping("produit/coef/{idPrd}/{idCoef}")
//    public Produit assignCoef(@PathVariable Long idPrd, @PathVariable Long idCoef){
//        Produit ExistProd=produitRepository.getOne(idPrd);
//        CoefRecProd assignCoef=coefRecProdRepository.getOne(idCoef);
//        List<CoefRecProd> lstCoef=ExistProd.getCoefRecProd();
//        lstCoef.add(assignCoef);
//        ExistProd.setCoefRecProd(lstCoef);
//        return produitRepository.saveAndFlush(ExistProd);
//    }
}
