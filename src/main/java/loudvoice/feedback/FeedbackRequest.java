package loudvoice.feedback;


import lombok.Data;

@Data
public class FeedbackRequest {
    private String about;
    private String name;
    private String email;
    private String message;
}
