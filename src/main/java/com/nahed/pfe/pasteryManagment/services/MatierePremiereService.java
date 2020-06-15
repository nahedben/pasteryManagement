package com.nahed.pfe.pasteryManagment.services;

import com.nahed.pfe.pasteryManagment.models.modelsCommon.MatierePremiere;
import com.nahed.pfe.pasteryManagment.repositories.stockRepositories.MatierePremierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatierePremiereService {

    @Autowired
    private MatierePremierRepository matierePremierRepository;

    public void updateMatPrem(MatierePremiere matierePremiere, Long id){
//
//        MatierePremiere mtPExist=matierePremierRepository.getOne(id);
//        BeanUtils.copyProperties(matierePremiere,mtPExist,"id");
//
//        stockLimitAlertManagement(mtPExist);
//        return matierePremierRepository.saveAndFlush(mtPExist);
//    }
//    public void stockLimitAlertManagement(MatierePremiere mat){
//        boolean existStAlert;
//        BigDecimal qtExist =mat.getQuantity();
//        BigDecimal stockLimExist= mat.getStockLimit();
//        if(qtExist.compareTo(stockLimExist)==1){
//            existStAlert =false;
//            mat.setStockLimitAlert(existStAlert);
//            mat.getStockLimit();
//        }else {
//            existStAlert=true;
//            mat.setStockLimitAlert(existStAlert);
//            mat.getStockLimit();
//        }
//    }
//    public void updatingList(List<MatierePremiere> list){
//        for ( MatierePremiere mt:list ) {
//            Long id=mt.getId();
//            //updateMatPrem(mt,id);
//            System.out.println(mt);}
//        }

//     public void updatedStockAlertList(){
//        List<MatierePremiere> mtList =matierePremierRepository.listMatWithqtlessLmt();
//                for(MatierePremiere mt: mtList){
//                    mt.setStockLimitAlert(true);
//                    Long id=mt.getId();
//                   updateMatPrem(mt,id);
//                }
    }




}
