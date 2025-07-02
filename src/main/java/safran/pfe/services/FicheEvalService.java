package safran.pfe.services;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import safran.pfe.entities.*;
import safran.pfe.exceptions.ResourceNotFoundException;
import safran.pfe.repo.*;

@Service
public class FicheEvalService {
	@Autowired

    private  FicheEvalRepository ficheEvalRepository;
	@Autowired

    private  StatutRepository statutRepository;
	@Autowired

    private  EntretienRepo entretienRepository;

    @Transactional
public FicheEval createFicheEvalForEntretien(Long entretienId) {
    Entretien entretien = entretienRepository.findById(entretienId)
            .orElseThrow(() -> new EntityNotFoundException("Entretien non trouvé"));
    
    if (entretien.getFicheEvalAssociee() != null) {
        throw new IllegalStateException("Une fiche existe déjà");
    }
    
    FicheEval ficheEval = new FicheEval();
    
    ficheEval.setFicheRh(new FicheEvalRh()); 
    ficheEval.setFicheTech(new FicheEvalTech());
    
    Statut statutInitial = statutRepository.findById(1L)
            .orElseThrow(() -> new RuntimeException("Statut par défaut non configuré"));
    ficheEval.setStatut(statutInitial);
    
    FicheEval savedFiche = ficheEvalRepository.save(ficheEval);
    
    entretien.setFicheEvalAssociee(savedFiche);
    entretienRepository.save(entretien);
    
    return savedFiche;
}
    
    
    public List<FicheEval> getAllFiches() {
        return ficheEvalRepository.findAll();
    }

    public Optional<FicheEval> getFicheById(Long id) {
        return ficheEvalRepository.findById(id);
    }

    public Optional<FicheEval> getFicheByEntretienId(Long entretienId) {
        return ficheEvalRepository.findByEntretienSpecifiqueId(entretienId);
    }
    
   /* @Transactional
    public FicheEval updateFicheEvalScore(Long ficheId) {
        // Chargez l'entité avec une requête fraîche
        FicheEval fiche = ficheEvalRepository.findById(ficheId)
                .orElseThrow(() -> new EntityNotFoundException("Fiche non trouvée"));
        
        // Forcez le recalcul manuellement
        double scoreRh = fiche.getFicheRh() != null ? fiche.getFicheRh().getScoreTotalRh() : 0;
        double scoreTech = fiche.getFicheTech() != null ? fiche.getFicheTech().getScoreTotalTech() : 0;
        double newScore = (scoreRh + scoreTech) / 2;
        
        fiche.setScoreTotal(newScore);
        
        // Sauvegardez explicitement
        fiche = ficheEvalRepository.save(fiche);
        
        // Rafraîchissez l'entité
        ficheEvalRepository.flush();
        
        return fiche;
    }*/
    
    @Transactional
    public FicheEval updateFicheEvalScore(Long ficheId) {
        FicheEval fiche = ficheEvalRepository.findById(ficheId)
                .orElseThrow(() -> new EntityNotFoundException("Fiche non trouvée"));
        
        // Calcul des scores
        double scoreRh = fiche.getFicheRh() != null ? fiche.getFicheRh().getScoreTotalRh() : 0;
        double scoreTech = fiche.getFicheTech() != null ? fiche.getFicheTech().getScoreTotalTech() : 0;
        double newScore = (scoreRh + scoreTech) / 2;
        fiche.setScoreTotal(newScore);
        
        // Détermination du statut global
        Statut statutGlobal = determineGlobalStatut(fiche);
        fiche.setStatutGlobal(statutGlobal);
        
        return ficheEvalRepository.save(fiche);
    }

    private Statut determineGlobalStatut(FicheEval fiche) {
        if (fiche.getFicheRh() == null || fiche.getFicheTech() == null || 
            fiche.getFicheRh().getStatut() == null || fiche.getFicheTech().getStatut() == null) {
            return statutRepository.findById(1L) // Statut "En Attente" par défaut
                    .orElseThrow(() -> new RuntimeException("Statut par défaut non trouvé"));
        }

        String statutRh = fiche.getFicheRh().getStatut().getNomStatut();
        String statutTech = fiche.getFicheTech().getStatut().getNomStatut();

        // Déterminer le statut global en fonction des combinaisons
        if (statutRh.equals("Retenu RH") && statutTech.equals("Retenu Tech")) {
            return statutRepository.findByNomStatut("Retenu Rh et Technique")
                    .orElseThrow(() -> new EntityNotFoundException("Statut non trouvé"));
        } else if (statutRh.equals("Retenu RH") && statutTech.equals("Non Retenu Tech")) {
            return statutRepository.findByNomStatut("Retenu RH non retenu technique")
                    .orElseThrow(() -> new EntityNotFoundException("Statut non trouvé"));
        } else if (statutRh.equals("Non Retenu RH") && statutTech.equals("Retenu Tech")) {
            return statutRepository.findByNomStatut("non Retenu RH et retenu technique")
                    .orElseThrow(() -> new EntityNotFoundException("Statut non trouvé"));
        } else if (statutRh.equals("Non Retenu RH") && statutTech.equals("Non Retenu Tech")) {
            return statutRepository.findByNomStatut("non Retenu RH et non retenu technique")
                    .orElseThrow(() -> new EntityNotFoundException("Statut non trouvé"));
        } else {
            return statutRepository.findById(1L) // Statut "En Attente" par défaut
                    .orElseThrow(() -> new RuntimeException("Statut par défaut non trouvé"));
        }
    }
    
    
    
    
}
/*
@Service
public class FicheEvalService {

    @Autowired
    private FicheEvalRepository ficheEvalRepository;
    
    @Autowired
    private FicheEvalRhRepository ficheRhRepository;
    
    @Autowired
    private FicheEvalTechRepository ficheTechRepository;

    public FicheEval createFicheEval() {
        // Créer et sauvegarder FicheEvalRh
        FicheEvalRh ficheRh = new FicheEvalRh();
        ficheRh = ficheRhRepository.save(ficheRh);
        
        // Créer et sauvegarder FicheEvalTech
        FicheEvalTech ficheTech = new FicheEvalTech();
        ficheTech = ficheTechRepository.save(ficheTech);
        
        // Créer FicheEval avec les associations
        FicheEval ficheEval = new FicheEval();
        ficheEval.setFicheRh(ficheRh);
        ficheEval.setFicheTech(ficheTech);
        
        return ficheEvalRepository.save(ficheEval);
    }
    
    @Transactional
    public FicheEval getFicheEvalById(Long id) {
        return ficheEvalRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fiche non trouvée"));
    }
}*/