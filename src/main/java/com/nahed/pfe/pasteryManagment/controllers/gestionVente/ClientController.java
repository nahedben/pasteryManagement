package com.nahed.pfe.pasteryManagment.controllers.gestionVente;

import com.nahed.pfe.pasteryManagment.models.modelVente.Client;
import com.nahed.pfe.pasteryManagment.repositories.venteRepositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class ClientController {
    @Autowired
    private ClientRepository clientRepositoriy;
    @PostMapping("client")
    public Client createClient(@RequestBody Client cl){
        return clientRepositoriy.saveAndFlush(cl);
    }
    @GetMapping("clients")
    public List<Client> RetreiveAllClient(){
        return clientRepositoriy.findAll();
    }
    @GetMapping("client/{id}")
    public ResponseEntity<Client> RetreiveCLientBId(@PathVariable Long id){
        Optional<Client> cl= clientRepositoriy.findById(id);
        if(cl.isPresent())
            return ResponseEntity.ok(cl.get());

        return ResponseEntity.notFound().build();

    }
    @GetMapping("client/phonenbr/{number}")
    public List<Client> retreiveClientByphone(@PathVariable String number){
        return clientRepositoriy.findByPhone(number);
    }
    @PutMapping("client/{id}")
    public Client editClient(@RequestBody Client cl, @PathVariable Long id){
        Client ExistCl=clientRepositoriy.getOne(id);
        if(cl.getNom()!=null){ExistCl.setNom(cl.getNom());}
        if(cl.getPrenom()!=null){ExistCl.setPrenom(cl.getPrenom());}
        if(cl.getEmail()!=null){ExistCl.setEmail(cl.getEmail());}
        if(cl.getPhone()!=null){ExistCl.setPhone(cl.getPhone());}
        if(cl.getAddress()!=null){ExistCl.setAddress(cl.getAddress());}
        if(cl.getTax()!=null){ExistCl.setTax(cl.getTax());}
        if(cl.getMatriculeFiscal()!=null){ExistCl.setMatriculeFiscal(cl.getMatriculeFiscal());}
        if(cl.getNote()!=null){ExistCl.setNote(cl.getNote());}
        if(cl.getCredit()!=null){ExistCl.setCredit(cl.getCredit());}
        return clientRepositoriy.saveAndFlush(ExistCl);
    }
    @DeleteMapping("client/{id}")
    public ResponseEntity<Client> removeClient(@PathVariable Long id){
        Optional<Client> cl=clientRepositoriy.findById(id);
        if(cl.isPresent()){
            clientRepositoriy.deleteById(id);
         return ResponseEntity.ok().build();}
       return  ResponseEntity.notFound().build();
    }
}
