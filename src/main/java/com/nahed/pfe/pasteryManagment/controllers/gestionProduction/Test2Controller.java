package com.nahed.pfe.pasteryManagment.controllers.gestionProduction;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



import java.util.*;


@RestController
@RequestMapping("api/v1")
public class Test2Controller {
    @GetMapping("/test2")
    public String appTest2() {


        //Model choco
        Model model = new Model();
        String myStr = "";

        //Disponibilité P1 et P2 sur la ligne de production
        int maxP1 = 100;
        int maxP2 = 100;

        //Poucentage de revenue sur P1 et P2 (%)
        int pourcentageP1 = 20;
        int pourcentageP2 = 25;

        //Commande1 (Passage de List<Integer> à IntVar[] qui est compris par choco)

        List<Integer> lstCl1 = new ArrayList<Integer>();

        lstCl1.add(10);
        lstCl1.add(20);

        IntVar[] testCommande1 = new IntVar[lstCl1.size()];
        for (int i = 0; i < lstCl1.size(); i++) {
            testCommande1[i] = model.intVar("a" + i, new int[]{0, lstCl1.get(i)});
        }

        //Commande2 (Passage de List<Integer> à IntVar[] qui est compris par choco)
        List<Integer> lstCl2 = new ArrayList<>();
        lstCl2.add(50);
        lstCl2.add(10);
        IntVar[] testCommande2 = new IntVar[lstCl2.size()];
        for (int i = 0; i < lstCl2.size(); i++) {
            testCommande2[i] = model.intVar("b" + i, new int[]{0, lstCl2.get(i)});
        }

        //Commande3 (Passage de List<Integer> à IntVar[] qui est compris par choco)
        List<Integer> lstCl3 = new ArrayList<>();
        lstCl3.add(5);
        lstCl3.add(5);
        IntVar[] testCommande3 = new IntVar[lstCl3.size()];
        for (int i = 0; i < lstCl3.size(); i++) {
            testCommande3[i] = model.intVar("c" + i, new int[]{0, lstCl3.get(i)});
        }

        //Sum of P1 <=maxP1 : testCommande1[0]=quantité de P1 demandée par Client1 | testCommande2[0]=quantité de P1 demandée par Client2 | testCommande3[0]=quantité de P1 demandée par Client3
        model.sum(new IntVar[]{testCommande1[0], testCommande2[0], testCommande3[0]}, "<=", maxP1).post();

        //Sum of P2 <=maxP2 testCommande1[1]=quantité de P2 demandée par Client1 | testCommande2[1]=quantité de P2 demandée par Client2 | testCommande3[1]=quantité de P2 demandée par Client3
        model.sum(new IntVar[]{testCommande1[1], testCommande2[1], testCommande3[1]}, "<=", maxP2).post();


        //Résolution TODO filtrer les résultats trouvé
        List<String> resLis = new ArrayList<String>();
        while (model.getSolver().solve()) {
            for (int i = 0; i < lstCl1.size(); i++) {
                String x = "Solution found : " + testCommande1[0] + ", " + testCommande2[0] + "," + testCommande3[0] + ", " + testCommande1[1] + ", " + testCommande2[1] + ", " + testCommande3[1];
                if (!resLis.contains(x) && testCommande1[0].getValue() + testCommande2[0].getValue() + testCommande3[0].getValue() != 0) {
                    resLis.add(x);
                    //Affichage des résultats et calcul d'heuristique : a0,a1 quantitiée respectivement de P1 et P2 pour Commande 1 |  b0,b1 quantitiée respectivement de P1 et P2 pour Commande 2 |  c0,c1 quantitiée respectivement de P1 et P2 pour Commande 3
                    myStr = x + " heuristique = " + (testCommande1[0].getValue() * pourcentageP1)
                            + (testCommande2[0].getValue() * pourcentageP2) / (testCommande1[0].getValue() + testCommande2[0].getValue() + testCommande3[0].getValue());
                }
            }
        }
        return myStr;
    }
}


