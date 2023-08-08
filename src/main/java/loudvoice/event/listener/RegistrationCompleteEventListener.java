package loudvoice.event.listener;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import loudvoice.event.RegistrationCompleteEvent;
import loudvoice.registration.token.VerificationTokenService;
import loudvoice.user.User;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final VerificationTokenService tokenService;
    private final JavaMailSender mailSender;
    private User user;
    SimpleDateFormat format = new SimpleDateFormat("E, dd-MM-yyyy");

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
       //1.get the user
        user = event.getUser();
        //2.generate a token for the user
        String vToken = UUID.randomUUID().toString();
        //3.save the token for the user
        tokenService.saveVerificationTokenForUser(user, vToken);
        //4.Build the verification url
        String url = event.getConfirmationUrl()+"/registration/verifyEmail?token=" + vToken;
        //5.send the email to the user
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e){
            throw new RuntimeException(e);
        }
    }
    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException{
        String subject = "Підтвердження електронної пошти";
        String senderName = "Служба перевірки користувачів";
        String mailContent = "<p> Привіт, " + user.getFirstName()+ ", </p>"
                + "<p>Дякуємо за реєстрацію у нас, "+""
                +"Будь ласка, перейдіть за посиланням нижче, щоб завершити реєстрацію.</p>"
                +"<a href=\"" + url + "\">Підтвердьте свою електронну адресу, щоб активувати обліковий запис<a/>"
                +"<p> Thank you <br> Users Registration Portal Service";
        emailMessage(subject, senderName, mailContent, mailSender, user);
    }

    public void sendPasswordResetVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException{
        String subject = "Password Reset Request Verification";
        String senderName = "Служба перевірки користувачів";
        String mailContent = "<p> Привіт, " + user.getFirstName()+ ", </p>"
                + "<p><b>Ви нещодавно надіслали запит на скидання пароля, </b> "+""
                +"Будь ласка, перейдіть за посиланням нижче, щоб завершити дію.</p>"
                +"<a href=\"" + url + "\">Скинути пароль<a/>"
                +"<p> Thank you <br> Users Registration Portal Service";
        emailMessage(subject, senderName, mailContent, mailSender, user);
    }

    public void sendTime(Date date , User user, String time) throws MessagingException, UnsupportedEncodingException{
        String subject = "Новий запис у журналі заннять";
        String senderName = "Учень додав час уроку";
        String mailContent = "<p> Привіт ;) " + "<br> </p>"
                + "<p>"+"Рита, заннятя записано на :"
                +format.format(date)+" о "+time + ", Від учня : "+user.getFirstName()+" "+user.getLastName()+".</p>"
                +"<p> Thank you <br> User Portal Service";
        emailTimeMessage(subject, senderName, mailContent, mailSender, user);
    }

    public void sendE(User user) throws MessagingException, UnsupportedEncodingException{
        String subject = "Вчитель додав вас до курсу";
        String senderName = "Loud Voice сервіс";
        String mailContent = "<p> Привіт, " + user.getFirstName()+ ";) </p>"
                + "<p><b> Ви записані до курсу Loud Voice, </b> "+""
                +" на сайті ви тепер можете обрати зручний для вас день та час заняття. </p>"
                +"<p> Thank you <br> User Portal Service";
        emailMessage(subject, senderName, mailContent, mailSender, user);
    }

    public void sendDelAdminTime(Date date , User user, String time) throws MessagingException, UnsupportedEncodingException{
        String subject = "Вчитель відмінив заняття";
        String senderName = "Loud Voice сервіс";
        String mailContent = "<p> Привіт! " + " </p>"
                + "<p>"+user.getFirstName()+",на жаль це заннятя було відмінено : "
                +format.format(date)+" о "+time + ".</p>"
                +"<p> Щоб дізнатися більше інформації зв'яжіться з вчителем або напишіть нам у зворотній зв'язок на сайті."
                +"<p> Thank you <br> User Portal Service";
        emailMessage(subject, senderName, mailContent, mailSender, user);
    }

    public void sendDelUserTime(Date date , User user, String time) throws MessagingException, UnsupportedEncodingException{
        String subject = "Учень відмінив заняття";
        String senderName = "Loud Voice сервіс";
        String mailContent = "<p> Привіт! " + " </p>"
                + "<p> Ваш учень "+user.getFirstName()+" "+user.getLastName()+" на жаль відмінив це заняття : "
                +format.format(date)+" о "+time + ".</p>"
                +"<p> Щоб дізнатися більше інформації зв'яжіться з учнем."
                +"<p> Thank you <br> User Portal Service";
        emailTimeMessage(subject, senderName, mailContent, mailSender, user);
    }



    private static void emailMessage(String subject, String senderName,
                                     String mailContent, JavaMailSender mailSender, User theUser)
        throws MessagingException, UnsupportedEncodingException{
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("example@gmail.com" , senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    private static void emailTimeMessage(String subject, String senderName,
                                     String mailContent, JavaMailSender mailSender, User theUser)
            throws MessagingException, UnsupportedEncodingException{
        MimeMessage messages = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(messages);
        messageHelper.setFrom("example@gmail.com" , senderName);
        messageHelper.setTo("example@gmail.com");
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(messages);
    }


}
