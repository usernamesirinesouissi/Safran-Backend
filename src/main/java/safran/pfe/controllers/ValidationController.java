package safran.pfe.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import safran.pfe.entities.NodeValidation;
import safran.pfe.entities.ValidationSeverity;
import safran.pfe.services.ValidationService;

@RestController
@RequestMapping("/api/validations")
public class ValidationController {

    @Autowired
    private ValidationService validationService;

    @GetMapping
    public ResponseEntity<List<NodeValidation>> getAllValidations() {
        return ResponseEntity.ok(validationService.getAllValidations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NodeValidation> getValidationById(@PathVariable String id) {
        Optional<NodeValidation> validation = validationService.getValidationById(id);
        return validation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NodeValidation> createValidation(@RequestBody NodeValidation validation) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(validationService.saveValidation(validation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NodeValidation> updateValidation(
            @PathVariable String id, @RequestBody NodeValidation validation) {

        if (!id.equals(validation.getId())) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(validationService.saveValidation(validation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteValidation(@PathVariable String id) {
        validationService.deleteValidation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-role/{role}")
    public ResponseEntity<List<NodeValidation>> getValidationsByRole(@PathVariable String role) {
        return ResponseEntity.ok(validationService.getValidationsByRole(role));
    }

    @GetMapping("/by-node/{nodeId}")
    public ResponseEntity<List<NodeValidation>> getValidationsByNodeId(@PathVariable String nodeId) {
        return ResponseEntity.ok(validationService.getValidationsByNodeId(nodeId));
    }

    @GetMapping("/severity-types")
    public ResponseEntity<List<ValidationSeverity>> getValidationSeverityTypes() {
        return ResponseEntity.ok(Arrays.asList(ValidationSeverity.values()));
    }
}


/*package safran.pfe.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import safran.pfe.entities.NodeValidation;
import safran.pfe.entities.ValidationSeverity;
import safran.pfe.services.ValidationService;

@RestController
@RequestMapping("/api/validations")
public class ValidationController {

    @Autowired
    private ValidationService validationService;

    @GetMapping
    public ResponseEntity<List<NodeValidation>> getAllValidations() {
        return ResponseEntity.ok(validationService.getAllValidations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NodeValidation> getValidationById(@PathVariable String id) {
        Optional<NodeValidation> validation = validationService.getValidationById(id);
        return validation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NodeValidation> createValidation(@RequestBody NodeValidation validation) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(validationService.saveValidation(validation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NodeValidation> updateValidation(
            @PathVariable String id, @RequestBody NodeValidation validation) {

        if (!id.equals(validation.getId())) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(validationService.saveValidation(validation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteValidation(@PathVariable String id) {
        validationService.deleteValidation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-role/{role}")
    public ResponseEntity<List<NodeValidation>> getValidationsByRole(@PathVariable String role) {
        return ResponseEntity.ok(validationService.getValidationsByRole(role));
    }

    @GetMapping("/by-node/{nodeId}")
    public ResponseEntity<List<NodeValidation>> getValidationsByNodeId(@PathVariable String nodeId) {
        return ResponseEntity.ok(validationService.getValidationsByNodeId(nodeId));
    }

    @GetMapping("/severity-types")
    public ResponseEntity<List<ValidationSeverity>> getValidationSeverityTypes() {
        return ResponseEntity.ok(Arrays.asList(ValidationSeverity.values()));
    }
} */