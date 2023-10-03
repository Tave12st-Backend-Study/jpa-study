package reviewjpa;

import lombok.*;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Setter
@Getter

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Period {

    // Period
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public boolean isWork(LocalDateTime startDate, LocalDateTime endDate) {
        return endDate.isAfter(startDate);
    }

}
