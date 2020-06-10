package com.nahed.pfe.pasteryManagment.controllers.gestionVente;

import com.nahed.pfe.pasteryManagment.models.modelVente.BonCommande;
import com.nahed.pfe.pasteryManagment.repositories.venteRepositories.BonCommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BonCommandeController {
    @Autowired
    private BonCommandeRepository bonCommandeRepository;
    @GetMapping("/BonCommandes")
    public List<BonCommande> retreiveAllBC(){
        return bonCommandeRepository.findAll();
    }
    @GetMapping("/BonCommande/{id}")
    public BonCommande retreiveBC(@PathVariable Long id){
        return bonCommandeRepository.getOne(id);
    }

    /***
     * Cette methode nous permet de creer un bon de comnmande
     * @param Bc
     * @return
     */
    @PostMapping("/BonCommande")
    public BonCommande createBC(@RequestBody BonCommande Bc) {
        return bonCommandeRepository.saveAndFlush(Bc);}

    /***
     *
     * Cette methode nous permet d'entrer le date souhaite et faie le calcule automatique de temps restant a
     * le reception de la commande par le client
     * @param Bc
     * @param id
     * @return
     */
     @PutMapping("/BonCommande/{id}")
     public BonCommande calculeTempRestantBonCommande(@RequestBody BonCommande Bc, @PathVariable Long id){
        BonCommande ExistBc= bonCommandeRepository.getOne(id);
        LocalDateTime start = LocalDateTime.now();
        ExistBc.setDateSouhaite(Bc.getDateSouhaite());
        LocalDateTime datSouh = ExistBc.getDateSouhaite();
        Duration tempRes = Duration.between(start, datSouh);
        long diff = tempRes.toMinutes();
        if (diff % 60 != 0) {
            long Restdiv = diff % 60;
            System.out.println(Restdiv);
            long main = (diff - Restdiv) / 60;
            System.out.println(+main + ":" + Restdiv);
        } else {
            if (diff / 60 < 1) {
                long result = diff;
                System.out.println("le temps restant a la commande est :" + result + "min");
            }
            long nbrH = diff / 60;
            System.out.println("Le temps restant a la commande est :" + nbrH + "H");

        }
        ExistBc.setTempsRestantLiv(diff);
        return bonCommandeRepository.saveAndFlush(ExistBc);

    }
    @PutMapping("/BonCommande/tempsRestant/{id}")
    public BonCommande updateTempsRestantBc(@PathVariable Long id){
        BonCommande ExistBc= bonCommandeRepository.getOne(id);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime datSouh = ExistBc.getDateSouhaite();
        Duration tempRes = Duration.between(start, datSouh);
        long diff = tempRes.toMinutes();
        if (diff % 60 != 0) {
            long Restdiv = diff % 60;
            System.out.println(Restdiv);
            long main = (diff - Restdiv) / 60;
            System.out.println(+main + ":" + Restdiv);
        } else {
            if (diff / 60 < 1) {
                long result = diff;
                System.out.println("le temps restant a la commande est :" + result + "min");
            }
            long nbrH = diff / 60;
            System.out.println("Le temps restant a la commande est :" + nbrH + "H");

        }
        ExistBc.setTempsRestantLiv(diff);
        return bonCommandeRepository.saveAndFlush(ExistBc);

    }
    @PutMapping("/BonCommande/putInf/{id}")
    public BonCommande addtheInf(@PathVariable Long id, @RequestBody BonCommande bc){
         BonCommande ExistBc= bonCommandeRepository.getOne(id);
         if(bc.getAvance()!=null){
             ExistBc.setAvance(bc.getAvance());
         }
         if(bc.getInfoSupp()!=null){
             ExistBc.setInfoSupp(bc.getInfoSupp());
         }
         return bonCommandeRepository.saveAndFlush(ExistBc);
    }
    @DeleteMapping("/BonCommande/{id}")
    public void removeBc(@PathVariable Long id){
         bonCommandeRepository.deleteById(id);
    }

}
