package safran.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import safran.pfe.entities.Candidat;
import safran.pfe.entities.Candidature;
import safran.pfe.entities.DTO.CandidatureDTO;
import safran.pfe.repo.CandidatRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/candidat")
@CrossOrigin("*")
public class CandidatController {

    @Autowired
    private  CandidatRepository candidatRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping
    public ResponseEntity<Candidat> createCandidat(@RequestBody Candidat candidat) {
        String encodedPassword = passwordEncoder.encode(candidat.getPassword());
        candidat.setPassword(encodedPassword);

        Candidat saved = candidatRepository.save(candidat);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/upload")
    public ResponseEntity<Candidat> createCandidatWithFiles(
            @RequestParam("nomCandidat") String nom,
            @RequestParam("prenomCandidat") String prenom,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("cv") MultipartFile cvFile,
            @RequestParam("photo") MultipartFile photoFile
    ) {

        try {
            Candidat candidat = new Candidat();
            candidat.setNomCandidat(nom);
            candidat.setPrenomCandidat(prenom);
            candidat.setEmail(email);
            candidat.setPassword(passwordEncoder.encode(password));


            Candidat savedCandidat = candidatRepository.save(candidat);


            new File(uploadDir).mkdirs();

            // Save CV file
            if (!cvFile.isEmpty()) {
                String cvFileName = savedCandidat.getId() + "_cv_" + sanitizePathTraversal(cvFile.getOriginalFilename());
                File cv = new File(uploadDir + cvFileName);
                cvFile.transferTo(cv);
                savedCandidat.setCvPath(uploadDir + cvFileName);
            }

            // Save Photo file
            if (!photoFile.isEmpty()) {
                String photoFileName = savedCandidat.getId() + "_photo_" + sanitizePathTraversal(photoFile.getOriginalFilename());
                File photo = new File(uploadDir + photoFileName);
                photoFile.transferTo(photo);
                savedCandidat.setPhotoPath(uploadDir + photoFileName);
            }

            savedCandidat = candidatRepository.save(savedCandidat);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedCandidat);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String sanitizePathTraversal(String filename) {
        return filename.replaceAll("[\\\\/:*?\"<>|]", "_");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCandidat(@PathVariable Long id) {
        Optional<Candidat> candidatureOpt = candidatRepository.findById(id);

        if (candidatureOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Candidat with ID " + id + " not found.");
        }

        candidatRepository.deleteById(id);
        return ResponseEntity.ok("Candidat with ID " + id + " deleted successfully.");
    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<String> getImage(@PathVariable String imageName) throws IOException {
        String imagePath = uploadDir + sanitizePathTraversal(imageName);
        Resource imageResource = new FileSystemResource(imagePath);

        if (!imageResource.exists()) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = Files.readAllBytes(imageResource.getFile().toPath());
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);


        String extension = imageName.substring(imageName.lastIndexOf('.') + 1).toLowerCase();
        String mimeType;

        if (extension.equals("pdf")) {
            mimeType = "application/pdf";
        } else if (extension.equals("jpg") || extension.equals("jpeg")) {
            mimeType = "image/jpeg";
        } else if (extension.equals("png")) {
            mimeType = "image/png";
        } else if (extension.equals("gif")) {
            mimeType = "image/gif";
        } else {
            mimeType = "application/octet-stream";
        }

        return ResponseEntity.ok("data:" + mimeType + ";base64," + base64Image);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Candidat> getCandidatById(@PathVariable Long id) {
        Optional<Candidat> candidat = candidatRepository.findById(id);
        return candidat.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/candidatures")
    public ResponseEntity<List<CandidatureDTO>> getCandidaturesByCandidatId(@PathVariable Long id) {
        Optional<Candidat> candidat = candidatRepository.findById(id);
        if (candidat.isPresent()) {
            List<CandidatureDTO> candidatureDTOList = candidat.get().getCandidatures().stream()
                    .map(candidature -> new CandidatureDTO(
                            candidature.getId(),
                            candidature.getDateDepot(),
                            candidature.getScore(),
                            candidature.getDemande().getReference(),
                            candidature.getDemande().getIntituleProjet(),
                            candidature.getStatutCandidature().getNom()
                    ))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(candidatureDTOList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
