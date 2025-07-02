package safran.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import safran.pfe.entities.DTO.DocumentResponseDTO; // Assurez-vous que cet import est présent
import safran.pfe.entities.Document;
import safran.pfe.entities.TypeDocument;
import safran.pfe.repo.CandidatureRepository;
import safran.pfe.repo.DocumentRep;
import safran.pfe.repo.TypeDocumentRepo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List; // Import nécessaire
import java.util.UUID;
import java.util.stream.Collectors; // Import nécessaire

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

   
    
    @Autowired
    private DocumentRep documentRepository;
    
    @Autowired
    private CandidatureRepository candidatureRepository; // Ajout nécessaire
    
    @Autowired
    private TypeDocumentRepo typeDocumentRepository; // Ajout nécessaire
    
    private Path rootLocation;   
    // NOUVELLE MÉTHODE POUR RÉCUPÉRER TOUS LES DOCUMENTS
    /**
     * Récupère les métadonnées de tous les documents présents dans le système.
     * @return Une liste de DTOs représentant chaque document.
     */
    @GetMapping
    public ResponseEntity<List<DocumentResponseDTO>> getAllDocuments() {
        // Utilisez une requête avec jointures pour charger les relations nécessaires
        List<Document> documents = documentRepository.findAllWithCandidatureAndCandidat();
        
        List<DocumentResponseDTO> documentDTOs = documents.stream()
                .map(DocumentResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(documentDTOs);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document non trouvé : " + id));

        try {
            Path filePath = Paths.get(document.getCheminStockage());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getNomFichier() + "\"")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                throw new RuntimeException("Impossible de lire le fichier: " + document.getNomFichier());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Erreur de chemin de fichier: " + document.getNomFichier(), e);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) throws IOException {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document non trouvé : " + id));

        // Supprimer le fichier physique
        Path filePath = Paths.get(document.getCheminStockage());
        Files.deleteIfExists(filePath);

        // Supprimer l'entrée en base de données
        documentRepository.delete(document);

        return ResponseEntity.noContent().build();
    }
    
    
    
    @PutMapping("/{id}/type")
    public ResponseEntity<DocumentResponseDTO> updateDocumentType(
            @PathVariable Long id,
            @RequestParam Long newTypeId) {
        
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document non trouvé : " + id));
        
        TypeDocument newType = typeDocumentRepository.findById(newTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Type de document non trouvé : " + newTypeId));
        
        document.setType(newType);
        Document updatedDocument = documentRepository.save(document);
        
        // Recharger avec les relations
        Document fullDocument = documentRepository.findByIdWithRelations(updatedDocument.getId())
                .orElseThrow(() -> new RuntimeException("Document non trouvé après mise à jour"));
        
        return ResponseEntity.ok(new DocumentResponseDTO(fullDocument));
    }

    // Méthode PUT pour remplacer le fichier d'un document
    @PutMapping("/{id}/file")
    public ResponseEntity<DocumentResponseDTO> replaceDocumentFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile newFile) {
        
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document non trouvé : " + id));
        
        // Supprimer l'ancien fichier
        try {
            Path oldFilePath = Paths.get(document.getCheminStockage());
            Files.deleteIfExists(oldFilePath);
        } catch (IOException e) {
            throw new RuntimeException("Échec de la suppression de l'ancien fichier", e);
        }
        
        // Stocker le nouveau fichier
        try {
            String originalFilename = newFile.getOriginalFilename();
            String storedFilename = UUID.randomUUID().toString() + "_" + originalFilename;
            Path destinationFile = this.rootLocation.resolve(storedFilename).normalize().toAbsolutePath();
            
            // Vérification sécurité
            if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
                throw new RuntimeException("Tentative de stockage en dehors du dossier autorisé");
            }
            
            newFile.transferTo(destinationFile);
            
            // Mettre à jour le document
            document.setNomFichier(originalFilename);
            document.setCheminStockage(destinationFile.toString());
            Document updatedDocument = documentRepository.save(document);
            
            // Recharger avec les relations
            Document fullDocument = documentRepository.findByIdWithRelations(updatedDocument.getId())
                    .orElseThrow(() -> new RuntimeException("Document non trouvé après mise à jour"));
            
            return ResponseEntity.ok(new DocumentResponseDTO(fullDocument));
            
        } catch (IOException e) {
            throw new RuntimeException("Échec du stockage du nouveau fichier", e);
        }
    }

    // Méthode PUT complète pour modifier à la fois le type et le fichier
    @PutMapping("/{id}")
    public ResponseEntity<DocumentResponseDTO> updateDocument(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile newFile,
            @RequestParam(value = "newTypeId", required = false) Long newTypeId) {
        
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document non trouvé : " + id));
        
        boolean changesMade = false;
        
        // Mise à jour du type si fourni
        if (newTypeId != null) {
            TypeDocument newType = typeDocumentRepository.findById(newTypeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Type de document non trouvé : " + newTypeId));
            document.setType(newType);
            changesMade = true;
        }
        
        // Mise à jour du fichier si fourni
        if (newFile != null && !newFile.isEmpty()) {
            try {
                // Supprimer l'ancien fichier
                Path oldFilePath = Paths.get(document.getCheminStockage());
                Files.deleteIfExists(oldFilePath);
                
                // Stocker le nouveau fichier
                String originalFilename = newFile.getOriginalFilename();
                String storedFilename = UUID.randomUUID().toString() + "_" + originalFilename;
                Path destinationFile = this.rootLocation.resolve(storedFilename).normalize().toAbsolutePath();
                
                // Vérification sécurité
                if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
                    throw new RuntimeException("Tentative de stockage en dehors du dossier autorisé");
                }
                
                newFile.transferTo(destinationFile);
                
                document.setNomFichier(originalFilename);
                document.setCheminStockage(destinationFile.toString());
                changesMade = true;
                
            } catch (IOException e) {
                throw new RuntimeException("Échec du remplacement du fichier", e);
            }
        }
        
        if (!changesMade) {
            throw new IllegalArgumentException("Aucune modification fournie");
        }
        
        Document updatedDocument = documentRepository.save(document);
        
        // Recharger avec les relations
        Document fullDocument = documentRepository.findByIdWithRelations(updatedDocument.getId())
                .orElseThrow(() -> new RuntimeException("Document non trouvé après mise à jour"));
        
        return ResponseEntity.ok(new DocumentResponseDTO(fullDocument));
    }
}