package safran.pfe.entities.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import safran.pfe.entities.FicheEval;
import safran.pfe.entities.Entretien; // Assurez-vous que Entretien est importé

@Data
@NoArgsConstructor
public class FicheEvalDetailResponseDTO {
    private Long id;                // ID de FicheEval
    private Long entretienId;
    private CandidatInfoDTO candidat;
    //private StatutDTO statut;       // Statut global de la FicheEval
    private Double scoreTotal;      // Score total de la FicheEval
    private FicheRhResponseDTO ficheRh; // Objet Fiche RH imbriqué
    private FicheTechResponseDTO ficheTech; // Objet Fiche Tech imbriqué
    
    // ... autres champs existants
    private StatutDTO statutGlobal;
    
    
    public StatutDTO getStatutGlobal() {
		return statutGlobal;
	}

	public void setStatutGlobal(StatutDTO statutGlobal) {
		this.statutGlobal = statutGlobal;
	}




	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}




	public Long getEntretienId() {
		return entretienId;
	}




	public void setEntretienId(Long entretienId) {
		this.entretienId = entretienId;
	}




	public CandidatInfoDTO getCandidat() {
		return candidat;
	}




	public void setCandidat(CandidatInfoDTO candidat) {
		this.candidat = candidat;
	}




	/*public StatutDTO getStatut() {
		return statut;
	}




	public void setStatut(StatutDTO statut) {
		this.statut = statut;
	}*/




	public Double getScoreTotal() {
		return scoreTotal;
	}




	public void setScoreTotal(Double scoreTotal) {
		this.scoreTotal = scoreTotal;
	}




	public FicheRhResponseDTO getFicheRh() {
		return ficheRh;
	}




	public void setFicheRh(FicheRhResponseDTO ficheRh) {
		this.ficheRh = ficheRh;
	}




	public FicheTechResponseDTO getFicheTech() {
		return ficheTech;
	}




	public void setFicheTech(FicheTechResponseDTO ficheTech) {
		this.ficheTech = ficheTech;
	}




	public static FicheEvalDetailResponseDTO fromEntity(FicheEval entity) {
        if (entity == null) return null;
        FicheEvalDetailResponseDTO dto = new FicheEvalDetailResponseDTO();
        dto.setId(entity.getId());

        Entretien entretien = entity.getEntretienSpecifique();
        if (entretien != null) {
            dto.setEntretienId(entretien.getId());
            if (entretien.getCandidature() != null && entretien.getCandidature().getCandidat() != null) {
                dto.setCandidat(CandidatInfoDTO.fromEntity(entretien.getCandidature().getCandidat()));
            }
        }

        dto.setStatutGlobal(StatutDTO.fromEntity(entity.getStatutGlobal()));
        dto.setScoreTotal(entity.getScoreTotal());
        dto.setFicheRh(FicheRhResponseDTO.fromEntity(entity.getFicheRh()));
        dto.setFicheTech(FicheTechResponseDTO.fromEntity(entity.getFicheTech()));

        return dto;
    }
}