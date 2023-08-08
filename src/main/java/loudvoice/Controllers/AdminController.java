package loudvoice.Controllers;


import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import loudvoice.event.listener.RegistrationCompleteEventListener;
import loudvoice.timeLessons.ILessonService;
import loudvoice.timeLessons.LessonRepository;
import loudvoice.timeLessons.timeLessons;
import loudvoice.user.IRoleService;
import loudvoice.user.IUserService;
import loudvoice.user.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/")
public class AdminController {
    private final IUserService userService;

    private final IRoleService roleService;
    private final ILessonService lessonService;
    private final LessonRepository lessonRepository;
    private final RegistrationCompleteEventListener eventListener;


    @GetMapping("users")
    public String getUsers(Model model){
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("students", userService.getStudents());
        return "users";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model){
        Optional<User> user = userService.findById(id);
        model.addAttribute("user", user.get());
        return "update-user";
    }

    @GetMapping("/addToStudent/{id}")
        public String addToStudent(@PathVariable("id") Long id){
        Optional<User> user = userService.findById(id);
        User user1 = userService.findByEmail(user.get().getEmail());
        if (user.get().getId() == 1){
            return "redirect:/admin/users?error_success";
        }
        if (!user.get().isEnabled()){
            return "redirect:/admin/users?error_notEnabled";
        }else {
            roleService.AddDelStudent(id);
            try {
                eventListener.sendE(user1);
            }catch (MessagingException | UnsupportedEncodingException e){
                System.out.println(e.getMessage());
            }
        }
        return "redirect:/admin/users?update_success";
    }

    @GetMapping("/backStudentToUser/{id}")
        public String backToUser(@PathVariable("id") Long id){
        roleService.DelStudent(id);
        return "redirect:/admin/users?update_success";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, User user){
        userService.updateUser(id, user.getFirstName(), user.getLastName(), user.getEmail());
        return "redirect:/admin/users?update_success";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        Optional<User> user = userService.findById(id);
        if (user.get().getId() == 1){
            return "redirect:/admin/users?error_success";
        }else {
        userService.deleteUser(id);
        }
        return "redirect:/admin/users?delete_success";
    }

    @GetMapping("addTime")
    public String addTime(Model model){
        model.addAttribute("timeL", lessonService.getAllTime());
        return "addTime";
    }

    @GetMapping("LessonsTime")
    public String LessonsTime(Model model){
        model.addAttribute("allTime", lessonService.getRecordedTime());
        return "LessonsTime";
    }
    @GetMapping("/deleteLessonsTime/{id}")
    public String deleteLessonsTimeTime(@PathVariable("id") Long id){
        lessonService.deleteLessonsTime(id);
        Optional<timeLessons> les = lessonService.findById(id);
        User user = userService.findByEmail(les.get().getUid());
        Date date = les.get().getDateIn();
        String time = les.get().getTimeIn();
        try {
            eventListener.sendDelAdminTime(date,user,time);
        }catch (MessagingException | UnsupportedEncodingException e){
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/LessonsTime?delete_success";
    }

    @PostMapping("TimeOk")
    public String TimeOk(@DateTimeFormat(pattern = "yyyy-MM-dd")@RequestParam Date dateIn, @RequestParam String timeIn, Model model) {
        String[] hourMin = timeIn.split(":");
        int EXPIRATION_hour = Integer.parseInt(hourMin[0]);
        int EXPIRATION_mins = Integer.parseInt(hourMin[1]);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateIn);
        calendar.add(Calendar.DAY_OF_WEEK,0);
        calendar.add(Calendar.HOUR,EXPIRATION_hour+1);
        calendar.add(Calendar.MINUTE,EXPIRATION_mins);
        Date exDate = new Date(calendar.getTime().getTime());
        timeLessons lessons = new timeLessons(dateIn,timeIn,exDate);
        lessonRepository.save(lessons);
        return "redirect:/admin/addTime?update_success";
    }

    @GetMapping("/deleteTime/{id}")
    public String deleteTime(@PathVariable("id") Long id){
        lessonService.deleteTime(id);
        return "redirect:/admin/addTime?delete_success";
    }
}
