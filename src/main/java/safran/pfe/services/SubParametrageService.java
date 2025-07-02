package safran.pfe.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import safran.pfe.entities.ParametrageWorkflow;
import safran.pfe.entities.SubParameterWorkflow;
import safran.pfe.repo.ParametrageRepository;
import safran.pfe.repo.SubParametrageRepository;

@Service
public class SubParametrageService {

    @Autowired
    private SubParametrageRepository subParametrageRepository;

    @Autowired
    private ParametrageRepository parametrageRepository;

    public SubParameterWorkflow ajouterSubParametrage(Long parametrageId, String nom) {
        ParametrageWorkflow parametrage = parametrageRepository.findById(parametrageId)
                .orElseThrow(() -> new RuntimeException("Paramétrage non trouvé"));

        SubParameterWorkflow subParametrage = new SubParameterWorkflow();
        subParametrage.setNom(nom);
        subParametrage.setParametrage(parametrage);

        return subParametrageRepository.save(subParametrage);
    }

    public List<SubParameterWorkflow> getSubParametrages(Long parametrageId) {
        return subParametrageRepository.findByParametrageId(parametrageId);
    }
}
