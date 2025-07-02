package safran.pfe.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import java.util.List;

@Entity
public class ParametrageWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @OneToMany(mappedBy = "parametrage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubParameterWorkflow> subParametrages;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public List<SubParameterWorkflow> getSubParametrages() {
        return subParametrages;
    }

    public void setSubParametrages(List<SubParameterWorkflow> subParametrages) {
        this.subParametrages = subParametrages;

        // Très important : on associe chaque sub à ce parametrage
        if (subParametrages != null) {
            for (SubParameterWorkflow sub : subParametrages) {
                sub.setParametrage(this);
            }
        }
    }
}
