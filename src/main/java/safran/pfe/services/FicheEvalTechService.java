package safran.pfe.services;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import safran.pfe.controllers.StatutController;
import safran.pfe.entities.*;
import safran.pfe.entities.DTO.ExigencesRequestDTO;
import safran.pfe.repo.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;


@Service
public class FicheEvalTechService {

    @Autowired
    private FicheEvalTechRepository ficheRepository;

    @Autowired
    private ExigenceTechniqueRepository exigenceRepository;

    @Autowired
    private FicheEvalTechRepository ficheEvalTechRepository;

    @Autowired
    private StatutRepository statutRepository;

    public FicheEvalTech createFicheTech(FicheEvalTech fiche) {
        return ficheRepository.save(fiche);
    }

    public FicheEvalTech getFicheTechById(Long id) {
        return ficheRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fiche technique non trouvée"));
    }

    public double getScoreTotalTech(Long ficheId) {
        FicheEvalTech fiche = getFicheTechById(ficheId);
        return fiche.getScoreTotalTech();
    }

    @Transactional
    public FicheEvalTech addOrUpdateExigencesToFiche(Long ficheId, List<ExigencesRequestDTO.ExigenceScoreDTO> exigences) {
        FicheEvalTech ficheTech = getFicheTechById(ficheId);

        if (ficheTech.getStatut() == null) {
            Optional<Statut> statutParDefaut = statutRepository.findByNomStatut("En Attente");
            if (statutParDefaut.isPresent()) {
                ficheTech.setStatut(statutParDefaut.get());
            } else {
                Statut nouveauStatut = new Statut();
                nouveauStatut.setNomStatut("En cours");
                ficheTech.setStatut(statutRepository.save(nouveauStatut));
            }
        }

        for (ExigencesRequestDTO.ExigenceScoreDTO dto : exigences) {
            ExigenceTechnique exigence = exigenceRepository.findById(dto.getExigenceId())
                .orElseThrow(() -> new RuntimeException("Exigence non trouvée: " + dto.getExigenceId()));

            Optional<FicheTechCompetence> existing = ficheTech.getCompetences().stream()
                .filter(c -> c.getExigenceTechnique().getId().equals(dto.getExigenceId()))
                .findFirst();

            if (existing.isPresent()) {
                existing.get().setScoreCompetenceTech(dto.getScore());
            } else {
                FicheTechCompetence ficheCompetence = new FicheTechCompetence();
                ficheCompetence.setFicheTech(ficheTech);
                ficheCompetence.setExigenceTechnique(exigence);
                ficheCompetence.setScoreCompetenceTech(dto.getScore());

                ficheTech.getCompetences().add(ficheCompetence);
                exigence.getFicheTechCompetences().add(ficheCompetence); // si bidirectionnel
            }
        }

        return ficheRepository.save(ficheTech);
    }
    
    @Transactional
    public FicheEvalTech updateStatutFicheTech(Long ficheTechId, Long statutId) {
        FicheEvalTech fiche = ficheRepository.findById(ficheTechId)
            .orElseThrow(() -> new EntityNotFoundException("Fiche technique non trouvée"));
        
        Statut statut = statutRepository.findById(statutId)
            .orElseThrow(() -> new EntityNotFoundException("Statut non trouvé"));
        
        fiche.setStatut(statut);
        
        fiche.calculateScoreTech();
        
        fiche = ficheRepository.saveAndFlush(fiche);
        
        return ficheRepository.findById(ficheTechId).orElse(fiche);
    }
    
    
   
}
