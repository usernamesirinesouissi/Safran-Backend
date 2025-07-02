package safran.pfe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import safran.pfe.entities.Notification;
import safran.pfe.repo.NotificationRepo;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@CrossOrigin("*")
public class NotificationController {

    @Autowired
    private NotificationRepo notificationRepo;

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationRepo.findAll();
        return ResponseEntity.ok(notifications);
    }
    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotification() {
        List<Notification> unreadNotifications = notificationRepo.findByIsReadOrderByCreatedAtDesc(false);
        return ResponseEntity.ok(unreadNotifications);
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Long> getUnreadNotificationCount() {

        List<Notification> unreadNotifications = notificationRepo.findByIsReadOrderByCreatedAtDesc(false);
        return ResponseEntity.ok((long) unreadNotifications.size());
    }
    @PutMapping("/{id}/read")
    @Transactional
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long id) {
        Notification notification = notificationRepo.findById(id)
                .orElse(null); // Or orElseThrow for a 404

        if (notification == null) {
            return ResponseEntity.notFound().build();
        }

        if (!notification.isRead()) {
            notification.setRead(true);
            notificationRepo.save(notification);
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all")
    @Transactional
    public ResponseEntity<Void> markAllNotificationsAsRead() {

        List<Notification> unreadNotifications = notificationRepo.findByIsReadOrderByCreatedAtDesc(false);
        for (Notification notification : unreadNotifications) {
            notification.setRead(true);
        }
        notificationRepo.saveAll(unreadNotifications);
        return ResponseEntity.ok().build();
    }
}
