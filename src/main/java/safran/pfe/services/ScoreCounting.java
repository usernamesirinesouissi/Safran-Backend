package safran.pfe.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import safran.pfe.entities.*;

@Service
public class ScoreCounting {

    private static final Logger log = LoggerFactory.getLogger(ScoreCounting.class);

    int countSectionsPresent(String text) {
        String[] importantSections = {"compétence", "skills", "formation", "education", "langue", "languages", "certification","certifications", "projet", "project"};
        int score = 0;
        for (String section : importantSections) {
            if (text.toLowerCase().contains(section)) {
                log.info("Section '{}' found (+3 pts)", section);
                score += 3;
            }
        }
        return score;
    }


    int countExperienceKeywords(String text) {
        String[] keywords = {"stage", "stagiaire","intern","bénévole","club", "projet", "pfe", "internship", "experience", "emploi"};
        String normalized = text.toLowerCase();
        int count = 0;
        StringBuilder foundKeywords = new StringBuilder();

        for (String keyword : keywords) {
            int index = 0;
            while ((index = normalized.indexOf(keyword, index)) != -1) {
                count++;
                foundKeywords.append("  • Mot clé trouvé : '")
                        .append(keyword)
                        .append("' à la position ")
                        .append(index)
                        .append("\n");
                index += keyword.length();
            }
        }

        int score = count * 3;
        log.info("Nombre total d'apparitions de mots clés liés à l'expérience : {} ({} pts)", count, score);
        log.info("Détails des mots clés trouvés :\n{}", foundKeywords);
        return score;
    }

    private int computeMaxScore(Demande demande) {
        int max = 0;

        if (demande.getCompetencestechnique() != null)
            max += demande.getCompetencestechnique().size() * 10;

        if (demande.getCompetencescomportementale() != null)
            max += demande.getCompetencescomportementale().size() * 1;

        if (demande.getFormationacademique() != null)
            max += demande.getFormationacademique().size() * 1;

        if (demande.getPropecole() != null)
            max += demande.getPropecole().size() * 2;

        max += 9 * 3;
        max += 15 * 3;

        return max;
    }

    public int computeScoreAsPercentage(String cvText, Demande demande) {
        int score = 0;
        String normalizedCV = cvText.toLowerCase();

        // 1. Technical skills
        if (demande.getCompetencestechnique() != null) {
            for (ExigenceTechnique tech : demande.getCompetencestechnique()) {
                if (normalizedCV.contains(tech.getNomExigence().toLowerCase())) {
                    score += 10;
                    log.info("Matched technical skill '{}': +10 pts", tech.getNomExigence());
                }
            }
        }

        // 2. Soft skills
        if (demande.getCompetencescomportementale() != null) {
            for (CompetenceComportementale soft : demande.getCompetencescomportementale()) {
                if (normalizedCV.contains(soft.getNomCompetence().toLowerCase())) {
                    score += 1;
                    log.info("Matched soft skill '{}': +1 pt", soft.getNomCompetence());
                }
            }
        }

        // 3. Academic formations
        if (demande.getFormationacademique() != null) {
            for (FormationAcademique formation : demande.getFormationacademique()) {
                if (normalizedCV.contains(formation.getNomFormation().toLowerCase())) {
                    score += 1;
                    log.info("Matched academic formation '{}': +1 pt", formation.getNomFormation());
                }
            }
        }

        // 4. Preferred schools
        if (demande.getPropecole() != null) {
            for (PropositionEcole ecole : demande.getPropecole()) {
                if (normalizedCV.contains(ecole.getNomEcole().toLowerCase())) {
                    score += 2;
                    log.info("Matched preferred school '{}': +2 pts", ecole.getNomEcole());
                }
            }
        }

        // 5. Experience keywords (job titles + experience terms)
        int keywordScore = countExperienceKeywords(cvText);
        score += keywordScore;

        // 6. Sections present
        int sectionScore = countSectionsPresent(cvText);
        score += sectionScore;

        int maxScore = computeMaxScore(demande);

        log.info("Total raw score: {}", score);
        log.info("Max possible score: {}", maxScore);

        if (maxScore == 0) {
            log.warn("Max score is zero, returning 0%");
            return 0;
        }

        int percentage = (int) ((score * 100.0) / maxScore);
        log.info("Final score: {}%", percentage);
        return percentage;
    }
}