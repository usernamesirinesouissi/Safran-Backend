package safran.pfe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import safran.pfe.entities.*;
import safran.pfe.entities.DTO.CompetencesRequestDTO; // Assurez-vous que ce DTO est bien défini
import safran.pfe.repo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FicheEvalRhService {

    private static final Logger logger = LoggerFactory.getLogger(FicheEvalRhService.class);

    private final FicheEvalRhRepository ficheRepository;
    private final FicheEvalRepository ficheEvalRepository; // Pour valider ficheEvalId si besoin
    private final CompetenceComportementaleRepository competenceRepository;
    private final FicheRhCompetenceRepository ficheRhCompetenceRepository; // Pour la suppression explicite
    private final StatutRepository statutRepository;

    @Autowired
    public FicheEvalRhService(FicheEvalRhRepository ficheRepository,
                              FicheEvalRepository ficheEvalRepository,
                              CompetenceComportementaleRepository competenceRepository,
                              FicheRhCompetenceRepository ficheRhCompetenceRepository,
                              StatutRepository statutRepository) {
        this.ficheRepository = ficheRepository;
        this.ficheEvalRepository = ficheEvalRepository;
        this.competenceRepository = competenceRepository;
        this.ficheRhCompetenceRepository = ficheRhCompetenceRepository;
        this.statutRepository = statutRepository;
    }

    public List<FicheEvalRh> getAllFichesRh() {
        return ficheRepository.findAll();
    }
    
    
    /*@Transactional
    public FicheEvalRh updateStatutFicheRh(Long ficheEvalId, Long ficheRhId, Long statutId) {
        FicheEvalRh fiche = ficheRepository.findById(ficheRhId)
            .orElseThrow(() -> new EntityNotFoundException("Fiche RH non trouvée"));
        
        Statut statut = statutRepository.findById(statutId)
            .orElseThrow(() -> new EntityNotFoundException("Statut non trouvé"));
        
        fiche.setStatut(statut);
        
        return ficheRepository.save(fiche);
    }*/
    
    @Transactional
    public FicheEvalRh updateStatutFicheRh(Long ficheEvalId, Long ficheRhId, Long statutId) {
        // 1. Vérifications existantes
        ficheEvalRepository.findById(ficheEvalId)
            .orElseThrow(() -> new EntityNotFoundException("Fiche d'évaluation non trouvée"));
        
        FicheEvalRh fiche = ficheRepository.findById(ficheRhId)
            .orElseThrow(() -> new EntityNotFoundException("Fiche RH non trouvée"));
        
        Statut statut = statutRepository.findById(statutId)
            .orElseThrow(() -> new EntityNotFoundException("Statut non trouvé"));
        
        // 2. Mise à jour du statut
        fiche.setStatut(statut);
        
        // 3. FORCER le recalcul du score
        fiche.calculateScoreRh(); // Cette méthode doit être publique
        
        // 4. Sauvegarde explicite
        fiche = ficheRepository.save(fiche);
        
        // 5. Rafraîchir l'entité
        ficheRepository.flush();
        
        // 6. Recharger l'entité pour être sûr d'avoir les dernières valeurs
        fiche = ficheRepository.findById(ficheRhId).orElse(fiche);
        
        return fiche;
    }

    @Transactional // Important pour la création et la gestion des relations
    public FicheEvalRh createFicheRh(FicheEvalRh fiche) {
        logger.debug("Tentative de création de FicheEvalRh: {}", fiche);
        // Initialiser le statut si non fourni
        if (fiche.getStatut() == null) {
            Optional<Statut> statutOpt = statutRepository.findByNomStatut("En Attente");
            statutOpt.ifPresentOrElse(
                fiche::setStatut,
                () -> {
                    Statut newStatut = new Statut();
                    newStatut.setNomStatut("En Attente");
                    fiche.setStatut(statutRepository.save(newStatut));
                    logger.info("Statut initial 'En Attente' assigné à la nouvelle fiche RH.");
                }
            );
        }
        // Si des compétences sont passées à la création, les gérer correctement
        // Mais typiquement, une fiche est créée vide puis les compétences sont ajoutées.
        // Si des FicheRhCompetence sont déjà dans la collection, elles doivent être persistées
        // avec la FicheEvalRh si la cascade est bien configurée.
        if (fiche.getCompetences() != null) {
            for (FicheRhCompetence frc : fiche.getCompetences()) {
                frc.setFicheRh(fiche); // Assurer la bidirectionnalité
            }
        }

        return ficheRepository.save(fiche);
    }

    public FicheEvalRh getFicheRhById(Long id) {
        return ficheRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fiche RH non trouvée avec ID: " + id));
    }

    public double getScoreTotalRh(Long ficheId) {
        FicheEvalRh fiche = getFicheRhById(ficheId);
        // La méthode calculateScoreRh() est appelée par @PostLoad ou @PreUpdate/@PrePersist
        // Il est bon de s'assurer qu'elle est appelée si on accède directement.
        if (fiche.getScoreTotalRh() == null) {
             fiche.calculateScoreRh(); // Assurez-vous que cette méthode peut être appelée et est publique
        }
        return fiche.getScoreTotalRh() != null ? fiche.getScoreTotalRh() : 0.0;
    }

    @Transactional
    public FicheEvalRh addOrUpdateCompetencesToFiche(Long ficheEvalId, Long ficheRhId, List<CompetencesRequestDTO.CompetenceScoreDTO> competencesRequestList) {
        // Valider l'existence de FicheEval parente
        ficheEvalRepository.findById(ficheEvalId)
            .orElseThrow(() -> new RuntimeException("Fiche d'évaluation globale non trouvée avec ID: " + ficheEvalId));

        FicheEvalRh ficheRh = ficheRepository.findById(ficheRhId)
            .orElseThrow(() -> new RuntimeException("Fiche RH non trouvée avec ID: " + ficheRhId));

        logger.info("Mise à jour des compétences pour Fiche RH ID: {}, Fiche Eval ID: {}", ficheRhId, ficheEvalId);
        logger.debug("Compétences demandées: {}", competencesRequestList);

        // Gérer le statut
        if (ficheRh.getStatut() == null || "En Attente".equalsIgnoreCase(ficheRh.getStatut().getNomStatut())) {
            String targetStatutName = competencesRequestList.isEmpty() ? "En Attente" : "En cours";
            Optional<Statut> statutOpt = statutRepository.findByNomStatut(targetStatutName);
            if (statutOpt.isPresent()) {
                ficheRh.setStatut(statutOpt.get());
            } else {
                Statut nouveauStatut = new Statut();
                nouveauStatut.setNomStatut(targetStatutName);
                ficheRh.setStatut(statutRepository.save(nouveauStatut));
                logger.info("Statut '{}' assigné à la fiche RH ID {}.", targetStatutName, ficheRhId);
            }
        }

        Map<Long, Double> requestedCompetencesMap = competencesRequestList.stream()
            .collect(Collectors.toMap(
                CompetencesRequestDTO.CompetenceScoreDTO::getCompetenceId,
                CompetencesRequestDTO.CompetenceScoreDTO::getScore,
                (score1, score2) -> score2 // Garder le dernier en cas de doublons dans la requête (ne devrait pas arriver)
            ));

        // 1. Identifier les FicheRhCompetence à supprimer (celles en DB mais pas dans la requête)
        List<FicheRhCompetence> aSupprimer = ficheRh.getCompetences().stream()
            .filter(frc -> frc.getCompetence() != null && !requestedCompetencesMap.containsKey(frc.getCompetence().getId()))
            .collect(Collectors.toList());

        // 2. Supprimer
        if (!aSupprimer.isEmpty()) {
            logger.debug("Suppression de {} FicheRhCompetence: {}", aSupprimer.size(), aSupprimer.stream().map(frc -> frc.getCompetence().getNomCompetence()).collect(Collectors.toList()));
            for (FicheRhCompetence frc : aSupprimer) {
                // Retirer de la collection ET supprimer de la DB
                ficheRh.getCompetences().remove(frc); // Important si pas de orphanRemoval
                ficheRhCompetenceRepository.delete(frc);
            }
        }

        // 3. Mettre à jour les existantes ou ajouter les nouvelles
        for (CompetencesRequestDTO.CompetenceScoreDTO reqComp : competencesRequestList) {
            CompetenceComportementale competenceEntity = competenceRepository.findById(reqComp.getCompetenceId())
                .orElseThrow(() -> new RuntimeException("Compétence comportementale non trouvée avec ID: " + reqComp.getCompetenceId()));

            Optional<FicheRhCompetence> optFrc = ficheRh.getCompetences().stream()
                .filter(frc -> frc.getCompetence() != null && frc.getCompetence().getId().equals(reqComp.getCompetenceId()))
                .findFirst();

            if (optFrc.isPresent()) {
                // Mettre à jour le score
                FicheRhCompetence frcExistante = optFrc.get();
                if (frcExistante.getScoreCompetenceRh() != reqComp.getScore()) {
                     frcExistante.setScoreCompetenceRh(reqComp.getScore());
                     logger.debug("Mise à jour score pour compétence '{}' à {} pour Fiche RH ID {}", competenceEntity.getNomCompetence(), reqComp.getScore(), ficheRhId);
                }
            } else {
                // Ajouter une nouvelle FicheRhCompetence
                FicheRhCompetence nouvelleFrc = new FicheRhCompetence();
                nouvelleFrc.setFicheRh(ficheRh); // Lier à la FicheEvalRh parente
                nouvelleFrc.setCompetence(competenceEntity);
                nouvelleFrc.setScoreCompetenceRh(reqComp.getScore());
                ficheRh.getCompetences().add(nouvelleFrc); // Ajouter à la collection de la fiche parente
                logger.debug("Ajout nouvelle compétence '{}' avec score {} à Fiche RH ID {}", competenceEntity.getNomCompetence(), reqComp.getScore(), ficheRhId);
            }
        }
        // La méthode calculateScoreRh annotée de FicheEvalRh est responsable du calcul.
        // save(ficheRh) va déclencher les @PreUpdate/@PrePersist et cascader les changements aux FicheRhCompetence.
        return ficheRepository.save(ficheRh);
    }
}