package com.nahed.pfe.pasteryManagment.controllers.gestionProduction;


import com.nahed.pfe.pasteryManagment.models.modelsCommon.CompletLigneRecetMat;
import com.nahed.pfe.pasteryManagment.models.modelsCommon.CompletProduit;
import com.nahed.pfe.pasteryManagment.models.modelsCommon.MatierePremiere;
import com.nahed.pfe.pasteryManagment.models.modelsCommon.Product;
import com.nahed.pfe.pasteryManagment.models.modelsProd.LigneRecette;
import com.nahed.pfe.pasteryManagment.models.modelsProd.OrdreProduction;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Produit;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Recette;
import com.nahed.pfe.pasteryManagment.models.modelsStock.Famille;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.LigneRecetteRepository;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.OrdreProductionRepository;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.ProduitRepository;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.RecetteRepository;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.FamilleRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import javax.persistence.SecondaryTable;
import javax.persistence.Tuple;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import static org.aspectj.runtime.internal.Conversions.longValue;

@CrossOrigin
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
    @Transactional
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
    public Produit calculQtyProduit(@PathVariable Long idprd,@PathVariable Long idOp){
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
        Produit ExistPr=produitRepository.getOne(idr);
        return produitRepository.saveAndFlush(ExistPr);

    }
    @PutMapping("produit/image/{idprd}")
    public Produit saveProdwithuploadPhoto(@RequestParam(value = "imageFile",required = true) MultipartFile file,@PathVariable Long idprd)throws IOException {
        byte[] img=compressBytes(compressBytes(file.getBytes()));
        Produit prd=produitRepository.getOne(idprd);
        prd.setPhoto(img);
        return produitRepository.saveAndFlush(prd);
    }

    @GetMapping("/produit/image/{id}")
    public byte[] retreiveProductWithPhoto(@PathVariable Long id){

        Produit prd= produitRepository.getOne(id);
        byte[] img =decompressBytes(prd.getPhoto());
        return img;
    }
    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }
        // uncompress the image bytes before returning it to the angular application

    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }

    @Autowired
    private FamilleRepository familleRepository;

    @PutMapping("/produit/assignFamille/{idprd}/{idFam}")
    public Produit assignFamille(@PathVariable Long idprd, @PathVariable Long idFam){
       Produit prd = produitRepository.getOne(idprd);
        Famille fm= familleRepository.getOne(idFam);
        List<Produit> lstPr= new ArrayList<Produit>();
        lstPr=fm.getProduits();
        lstPr.add(prd);
        fm.setProduits(lstPr);
        fm.setNbrProd(longValue(lstPr.size()));
        prd.setFamille(fm);
        familleRepository.saveAndFlush(fm);
        return produitRepository.saveAndFlush(prd);
    }
@Autowired
private LigneRecetteRepository ligneRecetteRepository;
    @PostMapping("/produit/monocreation")
            public Produit createProduitComp(@RequestBody CompletProduit completProduit){
//        Produit prd=cmprd.getPr();
//        List<LigneRecetMatPre> lstLgnRecMat= cmprd.getLstLgnReMat();
//
//        Recette rc = cmprd.getRc();
//
//
//           Produit newPr= createProduit(prd);
//           newPr=assignFamille(newPr.getId(), idf);
//
//
//           RecetteController RecCont = new RecetteController();
//       Recette newRect= RecCont.createRecette(rc);
//
//       LigneRecetteController LgRcCtr= new LigneRecetteController();
//
//       for(LigneRecette lgnRecette: lstLgnRecMat){
//
//       LigneRecette newLgrec=LgRcCtr.addNewLignRec(lrcMat.getLg());
//      newLgrec= LgRcCtr.assignMatPremToLignRect( lrcMat.getMat().getId(),newLgrec.getId());
//      newLgrec=LgRcCtr.assignRecetteToLignRct(newLgrec.getId(),newRect.getId());
//      }
//     Recette finalRecette =RecCont.assignRecetteToProduit(newPr.getId(),newRect.getId());
//       return newPr;
        LigneRecetteController ligneRecetteController = new LigneRecetteController();
        RecetteController recetteController=new RecetteController();
        List<LigneRecette> ArrayLigneRec = new ArrayList<LigneRecette>();

     for(LigneRecette ligneRecette: completProduit.getListLigneRecette()){
   LigneRecette lignerecette=  ligneRecetteController.addNewLignRec(ligneRecette);
   ArrayLigneRec.add(lignerecette);
     }
     completProduit.getRecette().setLigneRecette(ArrayLigneRec);
    Recette recette = recetteController.createRecette(completProduit.getRecette());
    Produit produit = createProduit(completProduit.getProduit());
 return recetteController.assignRecetteToProduit(produit.getId(),recette.getId());
    }

//    @Autowired CompletLigneRecetMat completLigneRecetMat;


    }


