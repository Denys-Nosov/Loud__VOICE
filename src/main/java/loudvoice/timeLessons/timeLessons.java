package loudvoice.timeLessons;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class timeLessons {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateIn;
    private String timeIn;
    private Date expirationTime;
    private boolean isEnabled = false;

    @JoinColumn(name = "user_id")
    private String Uid;

    public timeLessons(Date dateIn, String timeIn ,Date expirationTime) {
        this.dateIn = dateIn;
        this.timeIn = timeIn;
        this.expirationTime = expirationTime;
    }
}
