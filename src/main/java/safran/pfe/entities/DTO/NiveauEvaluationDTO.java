
package safran.pfe.entities.DTO;

public class NiveauEvaluationDTO {
    private Long id;
    private String nomNiveau;
    // Ajoutez d'autres propriétés nécessaires

    // Constructeurs
    public NiveauEvaluationDTO() {}

    public NiveauEvaluationDTO(Long id, String nomNiveau) {
        this.id = id;
        this.nomNiveau = nomNiveau;
    }

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomNiveau() { return nomNiveau; }
    public void setNomNiveau(String nomNiveau) { this.nomNiveau = nomNiveau; }
}