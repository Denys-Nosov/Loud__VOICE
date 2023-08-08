package loudvoice.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import loudvoice.feedback.Feedback;
import loudvoice.feedback.FeedbackCompleteEvent;
import loudvoice.utility.UrlUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@RequestMapping("/feedback")
public class FeedbackController {

    private final ApplicationEventPublisher publisher;

    @GetMapping("/feedback-form")
    public String showFeedbackForm(Model model){
        return "feedback";
    }

    @PostMapping("/feedback-form")
    public String feedback(@RequestParam String about,@RequestParam String email, @RequestParam String name,
                           @RequestParam String message, HttpServletRequest request){
        Feedback feedback = new Feedback(about,name,email,message);
        publisher.publishEvent(new FeedbackCompleteEvent(feedback,UrlUtil.getApplicationUrl(request)));
        return "redirect:/feedback/feedback-form?success";

    }
}
