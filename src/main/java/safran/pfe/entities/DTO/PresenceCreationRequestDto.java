package safran.pfe.entities.DTO;

import com.sun.istack.NotNull;

import java.time.LocalDate;
import java.util.List;

public class PresenceCreationRequestDto {


    private Long candidatId;

    private List<LocalDate> presenceDates;

    public PresenceCreationRequestDto(Long candidatId, List<LocalDate> presenceDates) {
        this.candidatId = candidatId;
        this.presenceDates = presenceDates;
    }
    public PresenceCreationRequestDto() {
    }
    public Long getCandidatId() {
        return candidatId;
    }

    public void setCandidatId(Long candidatId) {
        this.candidatId = candidatId;
    }

    public List<LocalDate> getPresenceDates() {
        return presenceDates;
    }

    public void setPresenceDates(List<LocalDate> presenceDates) {
        this.presenceDates = presenceDates;
    }
}
