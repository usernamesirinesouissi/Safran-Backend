package safran.pfe.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safran.pfe.entities.Campaign;
import safran.pfe.services.CampaignService;
//import safran.pfe.services.EmailService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@CrossOrigin("*")

public class CampaignController {

    private final CampaignService campaignService;
    //private final EmailService emailService;

    public CampaignController(CampaignService campaignService /*EmailService emailService*/) {
        this.campaignService = campaignService;
        //this.emailService = emailService;
    }

    /*@PostMapping
    public ResponseEntity<Campaign> createCampaign(@RequestBody Campaign campaign) {
        campaign.setCreatedAt(LocalDateTime.now());
        Campaign savedCampaign = campaignService.save(campaign);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCampaign);
    }*/
    
    @PostMapping
    public ResponseEntity<Campaign> createCampaign(@RequestBody Campaign campaign) {
        campaign.setCreatedAt(LocalDateTime.now());
        Campaign savedCampaign = campaignService.save(campaign);
        
        //emailService.sendCampaignLaunchEmails(savedCampaign);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCampaign);
    }

    /*@PostMapping("/{id}/send-reminders")
    public ResponseEntity<Campaign> triggerReminders(@PathVariable Long id) {
        Campaign campaign = campaignService.findById(id);
        
        emailService.sendReminderEmails(campaign);
        campaign.setEmailsSent(true);
        campaign.setUpdatedAt(LocalDateTime.now());
        
        return ResponseEntity.ok(campaignService.save(campaign));
    }*/

    @PutMapping("/{id}")
    public ResponseEntity<Campaign> updateCampaign(@PathVariable Long id, @RequestBody Campaign campaignDetails) {
        Campaign existingCampaign = campaignService.findById(id);
        
        existingCampaign.setTypeStage(campaignDetails.getTypeStage());
        existingCampaign.setStartDate(campaignDetails.getStartDate());
        existingCampaign.setEndDate(campaignDetails.getEndDate());
        existingCampaign.setAcademicYear(campaignDetails.getAcademicYear());
        existingCampaign.setStatus(campaignDetails.getStatus());
        existingCampaign.setUpdatedAt(LocalDateTime.now());
        
        return ResponseEntity.ok(campaignService.save(existingCampaign));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        campaignService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Campaign> getAllCampaigns() {
        return campaignService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campaign> getCampaignById(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.findById(id));
    }
    
    
}