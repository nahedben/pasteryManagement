package com.nahed.pfe.pasteryManagment.controllers.gestionVente;

import com.nahed.pfe.pasteryManagment.models.modelVente.BonCommande;
import com.nahed.pfe.pasteryManagment.models.modelVente.Client;
import com.nahed.pfe.pasteryManagment.models.modelVente.LigneBonCommande;
import com.nahed.pfe.pasteryManagment.repositories.venteRepositories.BonCommandeRepository;
import com.nahed.pfe.pasteryManagment.repositories.venteRepositories.ClientRepository;
import com.nahed.pfe.pasteryManagment.repositories.venteRepositories.LigneBonCommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BonCommandeController {
    @Autowired
    private BonCommandeRepository bonCommandeRepository;
    @Autowired
    private LigneBonCommandeRepository ligneBonCommandeRepository;
    @GetMapping("/BonCommandes")
    public List<BonCommande> retreiveAllBC(){
        return bonCommandeRepository.findAll();
    }
    @GetMapping("/BonCommande/{id}")
    public BonCommande retreiveBC(@PathVariable Long id){
        return bonCommandeRepository.getOne(id);
    }
    @GetMapping("/BonCommande/{idbc}/ListLignBc")
    public List<LigneBonCommande> getAllLigneInTheSameBC(@PathVariable Long idbc){
        return ligneBonCommandeRepository.findAllByBonCommandeId(idbc);
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
            System.out.println(+main + ":" + Restdiv);}
            if ((diff / 60) < 1) {
                long result = diff;
                System.out.println("le temps restant a la commande est :" + result + "min");
            }else if(diff%60 !=0){
            long nbrH = (diff / 60);
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

    /**
     * methode pour calculer totalprice de bon de commande
     * @param id
     * @return
     */
    @PutMapping("/BonCommande/{id}/calculTotal")
    public BonCommande calculTotalBC(@PathVariable Long id){
         BonCommande bc = bonCommandeRepository.getOne(id);
         List<LigneBonCommande> lstLgBC=ligneBonCommandeRepository.findAllByBonCommandeId(id);
        BigDecimal totalCout=new BigDecimal("0");
         for(LigneBonCommande lr:lstLgBC){
             totalCout=totalCout.add(lr.getTotalLinePrice());
         }
         bc.setTotalPric(totalCout);
         return bonCommandeRepository.saveAndFlush(bc);
    }

    /**
     * Calculer le reste a payer pour un bon de commande
     * @param id
     * @return
     */
    @PutMapping("/BonCommande/{id}/restPayer")
    public BonCommande CalculRestPayer(@PathVariable Long id){
        BonCommande bc=bonCommandeRepository.getOne(id);
        BigDecimal tot=bc.getTotalPric();
        BigDecimal av=bc.getAvance();
        BigDecimal restPay=tot.subtract(av);
        bc.setRestPaye(restPay);
        return bonCommandeRepository.saveAndFlush(bc);
    }

    @Autowired
    private ClientRepository clientRepositoriy;

    /**
     * Assign client au Bon Commande
     * @param idbc
     * @param idcl
     * @return
     */
    @PutMapping("BonCommande/{idbc}/Client/{idcl}")
    public BonCommande assignClientToBC(@PathVariable Long idbc, @PathVariable Long idcl){
        BonCommande bc= bonCommandeRepository.getOne(idbc);
        Client cl=clientRepositoriy.getOne(idcl);
        bc.setClient(cl);
        return bonCommandeRepository.saveAndFlush(bc);
    }

    @DeleteMapping("/BonCommande/{id}")
    public void removeBc(@PathVariable Long id){
         bonCommandeRepository.deleteById(id);
    }

}
