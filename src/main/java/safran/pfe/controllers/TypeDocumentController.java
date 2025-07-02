package safran.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safran.pfe.entities.DTO.TypeDocumentRequestDTO;
import safran.pfe.entities.DTO.TypeDocumentResponseDTO;
import safran.pfe.repo.TypeDocumentRepo;
import safran.pfe.entities.TypeDocument;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/typedocuments")
@CrossOrigin(origins = "*") // À configurer plus précisément pour la production
public class TypeDocumentController {

    @Autowired
    private TypeDocumentRepo typeDocumentRepository;

    @PostMapping
    public ResponseEntity<TypeDocumentResponseDTO> createTypeDocument(@RequestBody TypeDocumentRequestDTO requestDTO) {
        TypeDocument newType = new TypeDocument();
        newType.setNomTypeDoc(requestDTO.getNomTypeDoc());
        
        TypeDocument savedType = typeDocumentRepository.save(newType);
        
        return new ResponseEntity<>(convertToResponseDTO(savedType), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TypeDocumentResponseDTO>> getAllTypeDocuments() {
        List<TypeDocumentResponseDTO> dtos = typeDocumentRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeDocumentResponseDTO> getTypeDocumentById(@PathVariable Long id) {
        TypeDocument type = typeDocumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TypeDocument non trouvé avec l'id : " + id));
        return ResponseEntity.ok(convertToResponseDTO(type));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeDocumentResponseDTO> updateTypeDocument(@PathVariable Long id, @RequestBody TypeDocumentRequestDTO requestDTO) {
        TypeDocument existingType = typeDocumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TypeDocument non trouvé avec l'id : " + id));
        
        existingType.setNomTypeDoc(requestDTO.getNomTypeDoc());
        TypeDocument updatedType = typeDocumentRepository.save(existingType);
        
        return ResponseEntity.ok(convertToResponseDTO(updatedType));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeDocument(@PathVariable Long id) {
        if (!typeDocumentRepository.existsById(id)) {
            throw new ResourceNotFoundException("TypeDocument non trouvé avec l'id : " + id);
        }
        // Attention : vous pourriez vouloir vérifier si ce type est utilisé avant de le supprimer.
        typeDocumentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Méthode de conversion privée
    private TypeDocumentResponseDTO convertToResponseDTO(TypeDocument typeDocument) {
        TypeDocumentResponseDTO dto = new TypeDocumentResponseDTO();
        dto.setId(typeDocument.getId());
        dto.setNomTypeDoc(typeDocument.getNomTypeDoc());
        return dto;
    }
}