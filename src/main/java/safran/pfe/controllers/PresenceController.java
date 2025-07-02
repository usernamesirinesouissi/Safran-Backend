package safran.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import safran.pfe.entities.Candidat;
import safran.pfe.entities.DTO.PresenceCreationRequestDto;
import safran.pfe.entities.Presence;
import safran.pfe.repo.CandidatRepository;
import safran.pfe.repo.PresenceRepo;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/presence")
@CrossOrigin("*")
public class PresenceController {

    @Autowired
    private PresenceRepo presenceRepo;
    @Autowired
    private CandidatRepository candidatRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<?> createOrUpdatePresences(
             @RequestBody PresenceCreationRequestDto requestDto) {

        if (requestDto.getCandidatId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Candidate ID ('candidatId') cannot be null.");
        }
        if (requestDto.getPresenceDates() == null || requestDto.getPresenceDates().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Presence dates list ('presenceDates') cannot be null or empty.");
        }

        try {
            Candidat candidat = candidatRepository.findById(requestDto.getCandidatId())
                    .orElseThrow(() -> new EntityNotFoundException("Candidat not found with id: " + requestDto.getCandidatId()));

            List<Presence> processedPresences = new ArrayList<>();

            for (LocalDate date : requestDto.getPresenceDates()) {
                if (date == null) {
                    // Option 1: Skip null dates silently or with a log
                    System.err.println("Warning: Null date found in presenceDates list for candidate " + requestDto.getCandidatId() + ". Skipping.");
                    continue;
                }

                Optional<Presence> existingPresenceOpt = presenceRepo.findByCandidatIdAndDatePresence(candidat.getId(), date);

                Presence presenceRecord;
                if (existingPresenceOpt.isPresent()) {
                    presenceRecord = existingPresenceOpt.get();
                } else {
                    presenceRecord = new Presence();
                    presenceRecord.setCandidat(candidat);
                    presenceRecord.setDatePresence(date);

                }
                processedPresences.add(presenceRepo.save(presenceRecord));
            }

            if (requestDto.getPresenceDates().size() == 1 && processedPresences.size() == 1) {
                return ResponseEntity.status(HttpStatus.CREATED).body(processedPresences.get(0));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(processedPresences); // Return list

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Log e.getMessage() or e.printStackTrace() for server-side debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/candidat/{candidatId}")
    public ResponseEntity<List<Presence>> getPresencesForCandidat(@PathVariable Long candidatId) {
        if (candidatId == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        List<Presence> presences = presenceRepo.findByCandidatIdOrderByDatePresenceAsc(candidatId);
        return ResponseEntity.ok(presences);
    }
}

