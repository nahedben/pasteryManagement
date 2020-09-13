package com.nahed.pfe.pasteryManagment.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nahed.pfe.pasteryManagment.models.modelVente.BonCommande;
import com.nahed.pfe.pasteryManagment.models.modelVente.Client;
import com.nahed.pfe.pasteryManagment.models.modelVente.LigneBonCommande;
import com.nahed.pfe.pasteryManagment.models.modelVente.LigneFacture;
import com.nahed.pfe.pasteryManagment.models.modelsProd.OrdreProduction;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Produit;
import com.nahed.pfe.pasteryManagment.models.modelsProd.Recette;
import com.nahed.pfe.pasteryManagment.models.modelsStock.Famille;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.OrdreProductionRepository;
import com.nahed.pfe.pasteryManagment.repositories.productionRepositories.ProduitRepository;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.FamilleRepository;
import com.nahed.pfe.pasteryManagment.repositories.venteRepositories.BonCommandeRepository;
import com.nahed.pfe.pasteryManagment.repositories.venteRepositories.ClientRepository;
import com.sun.jdi.LongValue;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.tags.HtmlEscapeTag;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.ToLongBiFunction;

import static java.time.LocalDateTime.now;
import static org.aspectj.runtime.internal.Conversions.floatValue;
import static org.aspectj.runtime.internal.Conversions.longValue;

@CrossOrigin
@RestController
@RequestMapping("api/v1")
public class PlanningControlleur {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private BonCommandeRepository bonCommandeRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private FamilleRepository familleRepository;

    @Autowired
    private OrdreProductionRepository ordreProductionRepository;

    @Transactional
    @GetMapping("/planning")
    public String  etatPlanning() {

//        List<OrdreProduction> lstOrprd = ordreProductionRepository.findAll();
        List<Client> lstClcons=clientRepository.findAll();
        List<BonCommande> lstBcCons=bonCommandeRepository.findAll();

        LocalDateTime date = now();
        PlanningEtud pl =new PlanningEtud();

////        List<Recette> lstRc= new ArrayList<Recette>();
////        System.out.println("La taille de la liste des recette"+lstRc.size());
////        List<Long> listObje = new ArrayList<>();
//        List<List> result= new ArrayList<List>();
//        List<Famille> lstFm= familleRepository.findAll();
////        List<Long> listQtyAttein= new ArrayList<Long>();
//        for (BonCommande bc : lstBcCons) {
//          Long hBc= pl.calculHeurMargeBc(bc);
////            Produit pprd=ordp.getRecette().getProduit();
////           Long id= pprd.getId();
//           List<LigneBonCommande> lstbc=bc.getLigneBonCommande();
//           for(LigneBonCommande lb: lstbc){
//               lb.getBonCommande();
//           }
//
//            produitRepository.getOne(id);
//           Long  hprd=pl.calculHeurMargeBc();
//           System.out.println("l'heuristic de produit"+hprd +" de produit : "+ ordp.getRecette().getProduit().getName());
//           BigDecimal qtyPrd=ordp.getQuantity();
//           Famille fmProd=ordp.getRecette().getProduit().getFamille();
//           Long xqty =qtyPrd.longValue();
//           Long x=new Long("0");
//           if(fmProd.getProdSaisonAttein()==null)
//               fmProd.setProdSaisonAttein(x);
//           fmProd.setProdSaisonAttein(fmProd.getProdSaisonAttein()+xqty);
//           lstFm.add(fmProd);
////           Long qtyatteind= fmProd.getProdSaisonAttein();
////           listQtyAttein.add(qtyatteind);
//
////            Recette rc = ordp.getRecette();
////            lstRc.add(rc);
////            Produit prd = rc.getProduit();
//
////            Famille fm= prd.getFamille();
//
////                Long qty=fm.getProdSaison();
////                listObje.add(qty);
//        }
////        result.add(lstRc);
////        result.add(listObje);
////        result.add(listQtyAttein);
//        LstObject lsFam= new LstObject(lstFm,"saison1");
//        List<LstObject> lstObjet =new ArrayList<LstObject>();
//        lstObjet.add(lsFam);
//
//        result.add(lstObjet);
////        List fqmilleorderd=retreiveName(lstFm);
////        result.add(fqmilleorderd);
//        return result;
        return "test";
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @EqualsAndHashCode
        @ToString
        public class PlanningEtud {
            List<OrdreProduction> lstOprd;
            List<Client> lstCl;
            Client clpl;

            List<BonCommande> lstbcCl;
            LocalDateTime date;

            public List<OrdreProduction> arrangerOrdPro(List<OrdreProduction> lstop) {

                return lstop;
            }

            //dispPrd*(0.35*hClt+0.4*MontBc+0.25*HMargBc)
            // Methode de calcule de marge dans un bon de commande
            public Long calculHeurMargeBc(BonCommande bc) {

                List<LigneBonCommande> lsLgnBc = bc.getLigneBonCommande();
                List<BigDecimal> lstMontmarge = new ArrayList<BigDecimal>();
                for (LigneBonCommande lgnbc : lsLgnBc) {
                    BigDecimal unitpriceP = lgnbc.getProduit().getUnitPrice();
                    Integer PerMarge = lgnbc.getProduit().getPercentageMargin();
                    BigDecimal qtyP = lgnbc.getQuantity();
                    BigDecimal montMarge = qtyP.multiply(unitpriceP.multiply(BigDecimal.valueOf(PerMarge)));
                    //mettre tous les revenu de chaque ligne en chiffre dans un tableau
                    lstMontmarge.add(montMarge);
                }
                BigDecimal totalMargeBc = new BigDecimal(0);
                //calculer le revenu total  dans un Bon de commande et le mettre dans une variable totalMargeBc
                for (BigDecimal e : lstMontmarge) {
                    totalMargeBc = totalMargeBc.add(e);
                }
                //calculer en terme de pourcentage  le Revenu dans un bon de commande
                BigDecimal AvgRevenuPer = (totalMargeBc.divide(bc.getTotalPric(), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));


                Integer PercMarg = AvgRevenuPer.intValueExact();
                Long HmyRev = new Long(0);
                if (PercMarg > 50) {
                    HmyRev = longValue(1);
                } else if (PercMarg > 35) {
                    HmyRev = longValue(2);
                } else if (PercMarg > 15) {
                    HmyRev = longValue(3);
                } else {
                    HmyRev = longValue(5);
                }
                System.out.println(" l'heuristique de" + HmyRev);
                return HmyRev;
            }

            //Methode pour calculer l'heuristique de bon de commande en se basant sur son Montant total
            public BigDecimal calculHeurMontantBc(BonCommande bc) {
                BigDecimal totBc = bc.getTotalPric();
                BigDecimal HTotBc = new BigDecimal(0);
                if (totBc.compareTo(BigDecimal.valueOf(800)) == 1 || totBc.compareTo(BigDecimal.valueOf(800)) == 0) {
                    HTotBc = BigDecimal.valueOf(1.1);

                } else if (totBc.compareTo(BigDecimal.valueOf(700)) == 1 || totBc.compareTo(BigDecimal.valueOf(700)) == 0) {
                    HTotBc = BigDecimal.valueOf(1);
                } else if (totBc.compareTo(BigDecimal.valueOf(500)) == 1 || totBc.compareTo(BigDecimal.valueOf(500)) == 0) {
                    HTotBc = BigDecimal.valueOf(0.7);
                } else if (totBc.compareTo(BigDecimal.valueOf(400)) == 1 || totBc.compareTo(BigDecimal.valueOf(400)) == 0) {
                    HTotBc = BigDecimal.valueOf(0.5);
                } else if (totBc.compareTo(BigDecimal.valueOf(300)) == 1 || totBc.compareTo(BigDecimal.valueOf(300)) == 0) {
                    HTotBc = BigDecimal.valueOf(0.35);
                } else if (totBc.compareTo(BigDecimal.valueOf(200)) == 1 || totBc.compareTo(BigDecimal.valueOf(200)) == 0) {
                    HTotBc = BigDecimal.valueOf(0.2);
                } else {
                    HTotBc = BigDecimal.valueOf(0.1);
                }
                System.out.println("L'heuristique Bon de commande" + HTotBc);
                return HTotBc;
            }

            //Methode de calcule de l'heuristique de client en se basant sur le Chiffre d'achat realise pat client
            public Long CalculHeurClient(List<Client> lsCl, Client clt) {
                BigDecimal max = new BigDecimal(0);
                System.out.println("ici le max apres instancition:" + max);
                BigDecimal ch = new BigDecimal(0);
                BigDecimal chClt = clt.getChiffreAchat();
                Long Hclt = new Long(0);
                BigDecimal t = new BigDecimal(0.5);


                for (Client cl : lsCl) {
                    ch = cl.getChiffreAchat();
                    System.out.println("le chiffre du client nom " + cl.getNom() + " est :" + ch);
                    System.out.println("le max est :" + max);

                    if (ch.compareTo(max) == 1 || ch.compareTo(max) == 0) {
                        max = ch;
                        System.out.println(" le max dans la boucle choisi est " + max);

                    }
                    System.out.println("Le maxium de chiffre realise est :" + max);
                }

                if (chClt.compareTo(max) == 1 || chClt.compareTo(max) == 0) {
                    Hclt = longValue(1);
                    System.out.println("L'heuristique choisi est  pour chClt >= max: " + Hclt);
//                a.divide(b, 2, RoundingMode.HALF_UP)
                } else if (((chClt.divide(max, 2, RoundingMode.HALF_UP)).compareTo(t)) == 1) {
                    Hclt = longValue(2);
                    System.out.println("L'heuristique choisi est  pour chClt > demi de max: " + Hclt);
                } else if ((chClt.divide(max, 2, RoundingMode.HALF_UP)).compareTo(BigDecimal.valueOf(0.2)) == 1) {
                    Hclt = longValue(3);
                    System.out.println("L'heuristique choisi est  pour chClt > 20% max: " + Hclt);
                } else {
                    Hclt = longValue(6);
                    System.out.println("L'heuristique choisi est  pour chClt <= 20%max: " + Hclt);
                }
                return Hclt;
            }

            public Long dispProd(BonCommande bc) {
                Long possibiProd = longValue(1);
                List<LigneBonCommande> lgns = bc.getLigneBonCommande();
                List<Famille> lsfm = familleRepository.findAll();
                List<Long> lstetat=new ArrayList<Long>();
                for (LigneBonCommande lg : lgns) {
                    BigDecimal Aprod = lg.getQuantity();
                    Produit pd = lg.getProduit();
                    Long prdParFam = pd.getFamille().getProdSaison();
                    Long prdParFamAttein = pd.getFamille().getProdSaisonAttein();
                    BigDecimal qtToReach = Aprod.add(BigDecimal.valueOf(prdParFamAttein));
                    Famille fm = pd.getFamille();
                    System.out.println("La valeur de la quantite reserve: " +prdParFamAttein);
                    System.out.println("La valeur de la quantite Aproduire: " +Aprod);
                    if((qtToReach).compareTo(BigDecimal.valueOf(prdParFam))==1){

                        possibiProd=longValue(0);
                        System.out.println("La valeur de Possibilite de production est :" +possibiProd + " pour le lg id: "+lg.getId());


                    }else{
                    possibiProd=longValue(1);
                        System.out.println("La valeur de Possibilite de production est :" +possibiProd + " pour le lg id: "+lg.getId());}
                    lstetat.add(possibiProd);
                    System.out.println("La table de possibilite de production contient :"+lstetat);
            }
                int count =0;

                for(Long e: lstetat){

                    if(e==0){
                        possibiProd=longValue(0);
                        break;
                    }
                    possibiProd=longValue(1);
                }
                if(possibiProd==1){
                    for(LigneBonCommande lg : lgns){
                        BigDecimal Aprod = lg.getQuantity();
                        Produit pd = lg.getProduit();
                        Long prdParFamAttein = pd.getFamille().getProdSaisonAttein();
                        BigDecimal qtToReach = Aprod.add(BigDecimal.valueOf(prdParFamAttein));
                        Famille fm = pd.getFamille();
                       fm.setProdSaisonAttein(longValue(qtToReach));
                       familleRepository.saveAndFlush(fm);
                    }
                }
                return   lstetat.get(0);
            }

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @EqualsAndHashCode
            @ToString
            public class LstObject {
                List<Famille> lstF;
                String name;

            }

            public List<Famille> retreiveName(List<Famille> lstFm) {
//        Player ronaldo = new ObjectMapper().readValue(jsonString, Player.class);
                Famille fmN = new Famille();
                List<Famille> lstResu = new ArrayList<Famille>();
                for (Famille f : lstFm) {
                    String s = f.getName();
                    Long l = f.getProdSaison();
                    Long latt = f.getProdSaisonAttein();
                    fmN.setProdSaisonAttein(latt);
                    fmN.setProdSaison(l);
                    fmN.setName(s);
                    lstResu.add(fmN);
                }

                return lstResu;
            }
        }
            @GetMapping("hClient/{id}")
            public Long calculheurclientAffichage(@PathVariable Long id) {
                Client cl = clientRepository.getOne(id);
                List<Client> lstCl = clientRepository.findAll();
                PlanningEtud pl = new PlanningEtud();
                return pl.CalculHeurClient(lstCl, cl);

            }

            @GetMapping("hRevenuBc/{id}")
            public Long calculeHeurReveBc(@PathVariable Long id) {
                BonCommande bc = bonCommandeRepository.getOne(id);
                PlanningEtud pl = new PlanningEtud();
                return pl.calculHeurMargeBc(bc);
            }

            @GetMapping("hMontantBc/{id}")
            public BigDecimal HeuristicMontBc(@PathVariable Long id) {
                BonCommande bc = bonCommandeRepository.getOne(id);
                PlanningEtud pl = new PlanningEtud();
                return pl.calculHeurMontantBc(bc);
            }

    @GetMapping("hProduction/{id}")
    public Long HeuristicDispProduction(@PathVariable Long id){
        BonCommande bc =bonCommandeRepository.getOne(id);
        PlanningEtud pl =new PlanningEtud();
        return pl.dispProd(bc);
    }

        }






