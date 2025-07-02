package safran.pfe.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import safran.pfe.entities.Campaign;
import safran.pfe.entities.CampaignStatus;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    @Query("SELECT c FROM Campaign c WHERE c.endDate BETWEEN :start AND :end")
    List<Campaign> findCampaignsEndingSoon(
        @Param("start") LocalDate start,
        @Param("end") LocalDate end
    );}