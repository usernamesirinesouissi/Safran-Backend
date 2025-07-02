/*package safran.pfe.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import safran.pfe.entities.Campaign;

//EmailService.java
@Service
public class EmailService {

 @Async 
 public void sendReminderEmails(Campaign campaign) {
     try {
        
         System.out.println("Rappels envoyés pour la campagne: " + campaign.getId());
         
     } catch (Exception e) {
         System.err.println("Erreur d'envoi pour la campagne " + campaign.getId() + ": " + e.getMessage());
     }
 }
}*/
/*
package safran.pfe.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import safran.pfe.entities.Campaign;
import safran.pfe.entities.User;
import safran.pfe.repo.CampaignRepository;
import safran.pfe.repo.UserRepository;
@Service
public class EmailService {
	  private final JavaMailSender mailSender;
	    private final UserRepository userRepository;

	    @Autowired
	    public EmailService(JavaMailSender mailSender, UserRepository userRepository) {
	        this.mailSender = mailSender;
	        this.userRepository = userRepository;
	    }

	    
	    @Async
    public void sendCampaignLaunchEmails(Campaign campaign) {
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            String subject = "Nouvelle campagne de stages : " + campaign.getAcademicYear();
            String content = buildLaunchEmailContent(user, campaign);
            sendEmail(user.getEmail(), subject, content);
        });
    }

    // 2. Email de rappel avant endDate
    @Async
    public void sendReminderEmails(Campaign campaign) {
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            String subject = "[Rappel] Formulaire de stage à soumettre avant " + campaign.getEndDate();
            String content = buildReminderEmailContent(user, campaign);
            sendEmail(user.getEmail(), subject, content);
        });
    }

    private void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    private String buildLaunchEmailContent(User user, Campaign campaign) {
        return "Bonjour " + user.getUsername() + ",\n\n" +
               "Une nouvelle campagne de stages est ouverte pour " + campaign.getAcademicYear() + "!\n" +
               "Veuillez soumettre votre formulaire avant le " + campaign.getEndDate() + ".\n\n" +
               "Cordialement,\nL'équipe des stages";
    }

    private String buildReminderEmailContent(User user, Campaign campaign) {
        return "Cher " + user.getUsername() + ",\n\n" +
               "Rappel : La campagne de stages se termine le " + campaign.getEndDate() + ".\n" +
               "Il reste " + daysRemaining(campaign) + " jours pour soumettre votre demande.\n\n" +
               "Cordialement,\nL'équipe des stages";
    }

    private long daysRemaining(Campaign campaign) {
        return ChronoUnit.DAYS.between(LocalDate.now(), campaign.getEndDate());
    }
}*/