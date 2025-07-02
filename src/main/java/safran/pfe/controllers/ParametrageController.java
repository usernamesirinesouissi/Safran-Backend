package safran.pfe.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import safran.pfe.entities.ExigenceTechnique;
import safran.pfe.entities.ParametrageWorkflow;
import safran.pfe.repo.ParametrageRepository;
import safran.pfe.services.ParametrageService;
@RestController
@RequestMapping("/api/parametrages")
@CrossOrigin("*")
public class ParametrageController {

    @Autowired
    private ParametrageService parametrageService;
    
    @Autowired 
    private ParametrageRepository ParametrageRepository;

    @PostMapping("/ajouter")
    public ResponseEntity<?> ajouterParametrageAvecSousParam(@RequestBody ParametrageWorkflow parametrage) {
        try {
            ParametrageWorkflow saved = parametrageService.ajouterParametrageComplet(parametrage);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erreur lors de l'ajout : " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ParametrageWorkflow> getParametrageById(@PathVariable Long id) {
        Optional<ParametrageWorkflow> parametrage = ParametrageRepository.findById(id);
        return parametrage.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public List<ParametrageWorkflow> getAllParams() {
        return ParametrageRepository.findAll();
    }
    

  


}
