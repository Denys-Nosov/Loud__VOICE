package loudvoice.feedback;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class FeedbackCompleteEventListener implements ApplicationListener<FeedbackCompleteEvent> {
    private Feedback feedback;
    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(FeedbackCompleteEvent event) {
        feedback = event.getFeedback();
        String url = event.getConfirmationUrl();
        try {
            sendFeedbackEmail(url);
        }catch (MessagingException | UnsupportedEncodingException e){
            throw new RuntimeException(e);
        }
    }
    private static void emailFeedbackMessage(String subject, String senderName,
                                             String mailContent, JavaMailSender mailSender)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("example@gmail.com" , senderName);
        messageHelper.setTo("example@gmail.com");
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent , true);
        mailSender.send(message);
    }
    public void sendFeedbackEmail(String url) throws MessagingException, UnsupportedEncodingException{
        String subject = feedback.getAbout();
        String senderName = feedback.getName();
        String mailContent = "<p> Повідомлення від, " + feedback.getEmail() + ", </p>"
                + "<p> Hi, " + feedback.getMessage() + ", </p>";
        emailFeedbackMessage(subject, senderName, mailContent, mailSender);
    }

}
