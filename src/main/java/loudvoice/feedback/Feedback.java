package loudvoice.feedback;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Feedback  {
    private String about;
    private String name;
    private String email;
    private String message;

    public Feedback(String about, String name, String email, String message) {
        this.about = about;
        this.name = name;
        this.email = email;
        this.message = message;
    }
}
