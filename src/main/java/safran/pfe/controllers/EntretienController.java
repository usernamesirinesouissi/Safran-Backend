package safran.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import safran.pfe.entities.*;
import safran.pfe.repo.CandidatureRepository;
import safran.pfe.repo.EntretienRepo;
import safran.pfe.repo.StatutCandidatureRepository;
import safran.pfe.repo.StatutRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/entretien")
@CrossOrigin("*")
public class EntretienController {

    @Autowired
    private EntretienRepo entretienRepository;

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Autowired
    private StatutCandidatureRepository statutCandidatureRepository;

    @Autowired
    private StatutRepository statutRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<?> createEntretien(@RequestBody Entretien entretienRequest) {
        if (entretienRequest.getCandidature() == null || entretienRequest.getCandidature().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'ID de la candidature est requis.");
        }
        Optional<Candidature> candidatureOpt = candidatureRepository.findById(entretienRequest.getCandidature().getId());
        if (candidatureOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Candidature non trouvée avec l'ID : " + entretienRequest.getCandidature().getId());
        }

        entretienRequest.setCandidature(candidatureOpt.get());

        FicheEval newFicheEval = new FicheEval();

        Statut statutInitial = statutRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("Configuration erreur: Statut initial (ID 1) pour FicheEval non trouvé."));
        newFicheEval.setStatut(statutInitial);
        newFicheEval.setFicheRh(new FicheEvalRh());
        newFicheEval.setFicheTech(new FicheEvalTech());


        entretienRequest.setFicheEvalAssociee(newFicheEval);
        newFicheEval.setEntretienSpecifique(entretienRequest);

        Entretien savedEntretien = entretienRepository.save(entretienRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntretien);
    }

    @GetMapping("/by-candidat/{candidatId}")
    public List<Entretien> getEntretiensByCandidat(@PathVariable Long candidatId) {
        return entretienRepository.findByCandidature_Candidat_Id(candidatId);
    }

    @PostMapping("/test")
    public ResponseEntity<Entretien> createEntretien2(@RequestBody Entretien entretien) {
        Optional<Candidature> candidatureOpt = candidatureRepository.findById(entretien.getCandidature().getId());
        if (candidatureOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Candidature candidature = candidatureOpt.get();

        StatutCandidature statut2 = statutCandidatureRepository.findById(2L).orElseThrow(() -> new RuntimeException("Statut with ID 2 not found"));
        candidature.setStatutCandidature(statut2);

        List<Candidature> allCandidatures = candidatureRepository.findByCandidat(candidature.getCandidat());
        for (Candidature otherCandidature : allCandidatures) {
            if (!otherCandidature.getId().equals(candidature.getId())) {
                StatutCandidature statut3 = statutCandidatureRepository.findById(3L).orElseThrow(() -> new RuntimeException("Statut with ID 3 not found"));
                otherCandidature.setStatutCandidature(statut3);
                candidatureRepository.save(otherCandidature);
            }
        }

        candidatureRepository.save(candidature);

        entretien.setCandidature(candidature);
        Entretien saved = entretienRepository.save(entretien);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteEntretien(@PathVariable Long id) {
        Optional<Entretien> entretienOpt = entretienRepository.findById(id);
        if (entretienOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        entretienRepository.delete(entretienOpt.get());
        return ResponseEntity.noContent().build();
    }
}