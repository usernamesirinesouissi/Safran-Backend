package safran.pfe.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import safran.pfe.entities.ERole;
import safran.pfe.entities.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
  Optional<Role> findByName(ERole name);
  Optional<Role> findById(Long id);
}
