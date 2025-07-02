package safran.pfe.entities;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class FicheTechCompetence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fiche_tech_id")
    @JsonBackReference
    private FicheEvalTech ficheTech;

    @ManyToOne
    @JoinColumn(name = "competence_tech_id")
    private ExigenceTechnique exigenceTechnique;

    @Column(name = "score_competence_tech")
    private double scoreCompetenceTech;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FicheEvalTech getFicheTech() {
		return ficheTech;
	}

	public void setFicheTech(FicheEvalTech ficheTech) {
		this.ficheTech = ficheTech;
	}

	public ExigenceTechnique getExigenceTechnique() {
		return exigenceTechnique;
	}

	public void setExigenceTechnique(ExigenceTechnique exigenceTechnique) {
		this.exigenceTechnique = exigenceTechnique;
	}

	public double getScoreCompetenceTech() {
		return scoreCompetenceTech;
	}

	public void setScoreCompetenceTech(double scoreCompetenceTech) {
		this.scoreCompetenceTech = scoreCompetenceTech;
	}
    
    
    
}