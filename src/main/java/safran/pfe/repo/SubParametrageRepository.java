package safran.pfe.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import safran.pfe.entities.SubParameterWorkflow;

@Repository
public interface SubParametrageRepository extends JpaRepository<SubParameterWorkflow, Long> {
    List<SubParameterWorkflow> findByParametrageId(Long parametrageId);
}
