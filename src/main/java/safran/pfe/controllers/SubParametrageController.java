package safran.pfe.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import safran.pfe.entities.SubParameterWorkflow;
import safran.pfe.services.SubParametrageService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/subparametrages")
@CrossOrigin("*")
public class SubParametrageController {

    @Autowired
    private SubParametrageService subParametrageService;

    @PostMapping("/ajouter/{parametrageId}")
    public ResponseEntity<?> ajouterSubParametrage(@PathVariable Long parametrageId, @RequestBody SubParameterWorkflow subParametrage) {
        try {
            SubParameterWorkflow savedSubParametrage = subParametrageService.ajouterSubParametrage(parametrageId, subParametrage.getNom());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSubParametrage);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{parametrageId}")
    public ResponseEntity<List<SubParameterWorkflow>> getSubParametrages(@PathVariable Long parametrageId) {
        return ResponseEntity.ok(subParametrageService.getSubParametrages(parametrageId));
    }
}
