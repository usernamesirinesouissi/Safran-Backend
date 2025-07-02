package safran.pfe.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import safran.pfe.entities.Campaign;
import safran.pfe.entities.CampaignStatus;
import safran.pfe.repo.CampaignRepository;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;
    
    //private final EmailService emailService;

    public CampaignService(CampaignRepository campaignRepository
    		/*EmailService emailService*/) {
        this.campaignRepository = campaignRepository;
        //this.emailService = emailService;
    }


  

    public Campaign save(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    public Campaign findById(Long id) {
        return campaignRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Campagne non trouvée avec l'ID : " + id));
    }

    public List<Campaign> findAll() {
        return campaignRepository.findAll();
    }

    // Suppression
    public void deleteById(Long id) {
        campaignRepository.deleteById(id);
    }

    // Méthode existante de mise à jour du statut
    public Campaign updateCampaignStatus(Long id, CampaignStatus newStatus) {
        Campaign campaign = findById(id);
        campaign.setStatus(newStatus);
        campaign.setUpdatedAt(LocalDateTime.now());
        Campaign savedCampaign = save(campaign);
        
        if (newStatus == CampaignStatus.ACTIVE 
            && !savedCampaign.isEmailsSent() 
            && savedCampaign.getStartDate().isBefore(LocalDate.now().plusDays(1))) {
            //emailService.sendReminderEmails(savedCampaign);
        }
        return savedCampaign;
    }

   /* @Scheduled(cron = "0 0 8 * * ?")
    public void checkCampaignEndDates() {
        LocalDate reminderStart = LocalDate.now().plusDays(3);
        LocalDate reminderEnd = LocalDate.now().plusDays(7);
        
        List<Campaign> campaigns = campaignRepository.findCampaignsEndingSoon(
            reminderStart, 
            reminderEnd
        );
        
        campaigns.forEach(campaign -> {
            if (!campaign.isReminderSent()) {
                emailService.sendReminderEmails(campaign);
                campaign.setReminderSent(true);
                campaignRepository.save(campaign);
            }
        });
    } */}