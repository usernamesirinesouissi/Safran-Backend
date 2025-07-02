package safran.pfe.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;


import org.springframework.web.bind.annotation.*;

import safran.pfe.services.PdfTextExtractor;
import safran.pfe.services.ScoreCounting;

import safran.pfe.entities.*;
import safran.pfe.entities.DTO.DocumentResponseDTO;
import safran.pfe.repo.CandidatRepository;
import safran.pfe.repo.CandidatureRepository;
import safran.pfe.repo.DemandeRepository;
import safran.pfe.repo.DocumentRep;
import safran.pfe.repo.EvaluationStageRepo;
import safran.pfe.repo.StatutCandidatureRepository;
import safran.pfe.repo.TypeDocumentRepo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/api/candidature")
@CrossOrigin("*")
public class CandidatureController {

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Autowired
    private PdfTextExtractor pdfTextExtractor;

    @Autowired
    private ScoreCounting scoreCounting;

    @Autowired
    private CandidatRepository candidatRepository;

    @Autowired
    private DemandeRepository demandeRepository;
    @Autowired
    private StatutCandidatureRepository statutCandidatureRepository;
    private static final Logger log = LoggerFactory.getLogger(CandidatureController.class);

    
    @Autowired
    private EvaluationStageRepo evaluationStageRepository;
    
    @Autowired 
    private DocumentRep documentRepository;
    
    @Autowired 
    private TypeDocumentRepo typeDocumentRepository;
    
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    private Path rootLocation;    
    
    @PostConstruct
    public void init() {
        try {
            rootLocation = Paths.get(uploadDir);
            Files.createDirectories(rootLocation);
            System.out.println("Dossier de stockage initialisé : " + rootLocation.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Impossible d'initialiser le dossier de stockage !", e);
        }
    }
    
    @PostMapping("/{candidatureId}/documents")
    public ResponseEntity<List<DocumentResponseDTO>> uploadMultipleTypedDocuments(
            @PathVariable Long candidatureId,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("typeIds") List<Long> typeIds) {

        // Validation des paramètres
        if (files == null || typeIds == null || files.size() != typeIds.size()) {
            throw new IllegalArgumentException("Le nombre de fichiers doit correspondre au nombre de typeIds.");
        }

        // Récupération de la candidature
        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature non trouvée: " + candidatureId));
        
        List<Document> documentsToSave = new ArrayList<>();

        // Traitement de chaque fichier
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            Long typeId = typeIds.get(i);

            if (file.isEmpty()) {
                continue; // Ignorer les fichiers vides
            }
            
            // Récupération du type de document
            TypeDocument typeDocument = typeDocumentRepository.findById(typeId)
                    .orElseThrow(() -> new ResourceNotFoundException("TypeDocument non trouvé pour l'ID : " + typeId));

            try {
                // Génération d'un nom de fichier unique
                String originalFilename = file.getOriginalFilename();
                String storedFilename = UUID.randomUUID().toString() + "_" + originalFilename;
                Path destinationFile = this.rootLocation.resolve(storedFilename).normalize().toAbsolutePath();
                
                // Vérification de la sécurité du chemin
                if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
                    throw new RuntimeException("Tentative de stockage en dehors du dossier autorisé");
                }
                
                // Sauvegarde du fichier
                file.transferTo(destinationFile);
                
                // Création du document
                Document newDocument = new Document();
                newDocument.setNomFichier(originalFilename);
                newDocument.setCheminStockage(destinationFile.toString());
                newDocument.setCandidature(candidature);
                newDocument.setType(typeDocument);
                
                documentsToSave.add(newDocument);

            } catch (IOException e) {
                throw new RuntimeException("Échec du stockage du fichier " + file.getOriginalFilename(), e);
            }
        }

        // Sauvegarde des documents en base
        List<Document> savedDocuments = documentRepository.saveAll(documentsToSave);
        
        // Chargement complet des documents avec leurs relations
        List<DocumentResponseDTO> responseDTOs = savedDocuments.stream()
                .map(document -> {
                    // Rechargement du document avec toutes ses relations
                    Document fullDocument = documentRepository.findByIdWithRelations(document.getId())
                            .orElseThrow(() -> new RuntimeException("Document non trouvé après sauvegarde"));
                    return new DocumentResponseDTO(fullDocument);
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseDTOs, HttpStatus.CREATED);
    }

    
    @PostMapping("/postulescore/{candidatId}/{demandeId}")
    public ResponseEntity<?> createCandidaturewithscore(
            @PathVariable Long candidatId,
            @PathVariable Long demandeId
    ) {
        Optional<Candidat> candidatOpt = candidatRepository.findById(candidatId);
        Optional<Demande> demandeOpt = demandeRepository.findById(demandeId);

        if (candidatOpt.isEmpty() || demandeOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Candidat or Demande not found");
        }

        if (candidatureRepository.existsByCandidatIdAndDemandeId(candidatId, demandeId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Candidature already exists for this Candidat and Demande");
        }

        Optional<StatutCandidature> statutOpt = statutCandidatureRepository.findById(1L);
        if (statutOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Default StatutCandidature not found");
        }

        Candidat candidat = candidatOpt.get();
        Demande demande = demandeOpt.get();


        String cvPath = candidat.getCvPath();
        String cvText = pdfTextExtractor.extractTextFromPdf(cvPath);
        log.info("CV TEXT extracted: {}", cvText);
        int score =  scoreCounting.computeScoreAsPercentage(cvText, demande);

        Candidature candidature = new Candidature();
        candidature.setCandidat(candidat);
        candidature.setDemande(demande);
        candidature.setDateDepot(LocalDate.now());
        candidature.setScore(score);
        candidature.setStatutCandidature(statutOpt.get());

        Candidature saved = candidatureRepository.save(candidature);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    @PostMapping("/postule/{candidatId}/{demandeId}")
    public ResponseEntity<?> createCandidature(
            @PathVariable Long candidatId,
            @PathVariable Long demandeId
    ) {
        Optional<Candidat> candidatOpt = candidatRepository.findById(candidatId);
        Optional<Demande> demandeOpt = demandeRepository.findById(demandeId);

        if (candidatOpt.isEmpty() || demandeOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Candidat or Demande not found");
        }

        if (candidatureRepository.existsByCandidatIdAndDemandeId(candidatId, demandeId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Candidature already exists for this Candidat and Demande");
        }

        Optional<StatutCandidature> statutOpt = statutCandidatureRepository.findById(1L);
        if (statutOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Default StatutCandidature not found");
        }

        Candidature candidature = new Candidature();
        candidature.setCandidat(candidatOpt.get());
        candidature.setDemande(demandeOpt.get());
        candidature.setDateDepot(LocalDate.now());
        candidature.setScore(0);
        candidature.setStatutCandidature(statutOpt.get());

        Candidature saved = candidatureRepository.save(candidature);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Candidature> getAllCandidature() {
        return candidatureRepository.findAll();
    }

    @GetMapping("/statutaccepte")
    public List<Candidature> getCandidaturesWithStatutId() {

        return candidatureRepository.findByStatutCandidatureId(1L);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCandidature(@PathVariable Long id) {
        Optional<Candidature> candidatureOpt = candidatureRepository.findById(id);

        if (candidatureOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Candidature with ID " + id + " not found.");
        }

        candidatureRepository.deleteById(id);
        return ResponseEntity.ok("Candidature with ID " + id + " deleted successfully.");
    }

    
    
    @PutMapping("/candidatures/{id}/statut")
    public ResponseEntity<Candidature> updateStatutCandidature(
            @PathVariable Long id,
            @RequestParam Long statutId) {
        
        Optional<Candidature> candidatureOpt = candidatureRepository.findById(id);
        Optional<StatutCandidature> statutOpt = statutCandidatureRepository.findById(statutId);
        
        if (!candidatureOpt.isPresent() || !statutOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Candidature candidature = candidatureOpt.get();
        StatutCandidature newStatut = statutOpt.get();
        
        // Si le nouveau statut est "Affecté à un besoin" et qu'il n'y a pas encore d'évaluation
        if ("Affecté à un besoin".equals(newStatut.getNom()) && candidature.getEvaluationsStage() == null) {
            EvaluationStage newEvaluation = new EvaluationStage();
            newEvaluation.setCandidature(candidature);
            evaluationStageRepository.save(newEvaluation);
        }
        
        candidature.setStatutCandidature(newStatut);
        Candidature updatedCandidature = candidatureRepository.save(candidature);
        
        return ResponseEntity.ok(updatedCandidature);
    }
}
