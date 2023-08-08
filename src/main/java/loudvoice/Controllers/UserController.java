package loudvoice.Controllers;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import loudvoice.event.listener.RegistrationCompleteEventListener;
import loudvoice.timeLessons.ILessonService;
import loudvoice.timeLessons.LessonRepository;
import loudvoice.timeLessons.timeLessons;
import loudvoice.user.IUserService;
import loudvoice.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping("/user/")
public class UserController {
    private final ILessonService lessonService;
    private final LessonRepository lessonRepository;
    private final IUserService userService;
    private final RegistrationCompleteEventListener eventListener;

    @GetMapping("my-page")
    public String getUsers(User user ,Model model){
        model.addAttribute("user", user.getId());
        model.addAttribute("TimeL", lessonService.getAllMyTime());
        model.addAttribute("TIME", lessonService.getLessTime());
        return "userControlPage";
    }

    @GetMapping("/add-time/{id}")
    public String addTime(@PathVariable("id") Long id, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String Uid = principal.getName();
        lessonService.addDateId(Uid,id);
        User user = userService.findByEmail(Uid);
        Optional<timeLessons> les = lessonService.findById(id);
        Date date = les.get().getDateIn();
        String time = les.get().getTimeIn();
        try {
            eventListener.sendTime(date,user,time);
        }catch (MessagingException | UnsupportedEncodingException e){
            System.out.println(e.getMessage());
        }
        return "redirect:/user/my-page?update_success";
    }

    @GetMapping("/Del-time/{id}")
    public String DelTime(@PathVariable("id") Long id, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String Uid = principal.getName();
        lessonService.DeleteDateId(Uid,id);
        Optional<timeLessons> les = lessonService.findById(id);
        User user = userService.findByEmail(Uid);
        Date date = les.get().getDateIn();
        String time = les.get().getTimeIn();
        try {
            eventListener.sendDelUserTime(date,user,time);
        }catch (MessagingException | UnsupportedEncodingException e){
            System.out.println(e.getMessage());
        }
        return "redirect:/user/my-page?delete_success";
    }


}
