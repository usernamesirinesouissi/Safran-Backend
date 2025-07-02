package safran.pfe.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import safran.pfe.entities.Notification;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification,Long> {



    List<Notification> findByIsReadOrderByCreatedAtDesc(boolean isRead);
}
