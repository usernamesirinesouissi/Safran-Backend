package safran.pfe.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import safran.pfe.entities.ERole;
import safran.pfe.entities.User;
import safran.pfe.repo.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/by-role/{roleId}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable Integer roleId) {
        List<User> users = userRepository.findByRoles_Id(roleId);
        return ResponseEntity.ok(users);
    }
    
  

    // Nouveau endpoint pour recherche par nom de rôle
    @GetMapping("/by-role-name/{roleName}")
    public ResponseEntity<List<User>> getUsersByRoleName(@PathVariable String roleName) {
        List<User> users = userRepository.findByRoles_Name(ERole.valueOf(roleName));
        return ResponseEntity.ok(users);
    }
}