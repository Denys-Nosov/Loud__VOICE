package loudvoice.Controllers;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import loudvoice.event.listener.RegistrationCompleteEventListener;
import loudvoice.feedback.Feedback;
import loudvoice.feedback.FeedbackCompleteEvent;
import loudvoice.utility.UrlUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class MainController  {
    private final RegistrationCompleteEventListener eventListener;
    private final ApplicationEventPublisher publisher;

    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model){
        model.addAttribute("title", "О нас");
        return "about";
    }

    @GetMapping("/contacts")
    public String contact(Model model){
        model.addAttribute("title", "Контакти");
        return "Contacts";
    }

    @PostMapping("/contacts")
    public String feedback(@RequestParam String about, @RequestParam String email, @RequestParam String name,
                           @RequestParam String message, HttpServletRequest request){
        Feedback feedback = new Feedback(about,name,email,message);
        publisher.publishEvent(new FeedbackCompleteEvent(feedback, UrlUtil.getApplicationUrl(request)));
        return "redirect:/contacts?success";

    }

    @GetMapping("/our-Students")
    public String OurStudents(Model model){
        model.addAttribute("title", "Наші учні");
        return "OurStudents";
    }

    @GetMapping("/our-Rules")
    public String OurRules(Model model){
        model.addAttribute("title", "Правила викладача");
        return "Rules";
    }

    @GetMapping("/Education")
    public String Education(Model model){
        model.addAttribute("title", "Навчання");
        return "Education";
    }

    @GetMapping("/user-error-page")
    public String UserError(Model model){
        model.addAttribute("title", "О нас");
        return "UserErrorPage";
    }

    @GetMapping("/authorization")
    public String authorization(Model model){
        model.addAttribute("title", "Вход");
        return "login";
    }

    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("title", "Регистрация");
        return "registration";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }
}

