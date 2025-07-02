package safran.pfe.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import safran.pfe.entities.ERole;
import safran.pfe.entities.Role;
import safran.pfe.repo.RoleRepository;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }
    
 
    // Nouveau endpoint pour récupérer un rôle par nom
    @GetMapping("/by-name/{name}")
    public ResponseEntity<Role> getRoleByName(@PathVariable String name) {
        Role role = roleRepository.findByName(ERole.valueOf(name))
            .orElseThrow(() -> new RuntimeException("Role not found"));
        return ResponseEntity.ok(role);
    }
}