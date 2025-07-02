package safran.pfe.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import safran.pfe.entities.ERole;
import safran.pfe.entities.Role;
import safran.pfe.entities.User;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  
  List<User> findByRoles_Id(Integer roleId);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
  
  /*@Query("SELECT u FROM User u JOIN u.roles r WHERE r.id = :roleId")
  List<User> findByRoles_Id(@Param("roleId") Integer roleId);*/
  
  @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :name")
  List<User> findByRoles_Name(@Param("name") ERole name);
  
  List<User> findByRoles(Role role);
  
  // Ou avec une requête JPQL si la relation est complexe
  @Query("SELECT u FROM User u JOIN u.roles r WHERE r.id = :roleId")
  List<User> findByRoleId(@Param("roleId") Integer roleId);
}
