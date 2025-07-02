package safran.pfe.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import safran.pfe.entities.ParametrageWorkflow;
import safran.pfe.repo.ParametrageRepository;
@Service
public class ParametrageService {

    @Autowired
    private ParametrageRepository parametrageRepository;

    public ParametrageWorkflow ajouterParametrageComplet(ParametrageWorkflow parametrage) {
        // Associer chaque sous-param au parent
        if (parametrage.getSubParametrages() != null) {
            parametrage.getSubParametrages().forEach(sub -> sub.setParametrage(parametrage));
        }
        return parametrageRepository.save(parametrage);
    }

    public Optional<ParametrageWorkflow> getById(Long id) {
        return parametrageRepository.findById(id);
    }
    
    public List<ParametrageWorkflow> getAllParametrages() {
        return parametrageRepository.findAll();  // Assure-toi que le repo hérite bien de JpaRepository
    }

}
