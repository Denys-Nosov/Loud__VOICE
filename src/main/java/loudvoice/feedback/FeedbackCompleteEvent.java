package loudvoice.feedback;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class FeedbackCompleteEvent extends ApplicationEvent {
    private Feedback feedback;
    private String confirmationUrl;

    public FeedbackCompleteEvent(Feedback feedback, String confirmationUrl) {
        super(feedback);
        this.feedback = feedback;
        this.confirmationUrl = confirmationUrl;
    }
}
