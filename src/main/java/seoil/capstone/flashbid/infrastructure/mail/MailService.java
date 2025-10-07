package seoil.capstone.flashbid.infrastructure.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import seoil.capstone.flashbid.infrastructure.mail.exception.UAMailFailedException;


@Service
@RequiredArgsConstructor
@Slf4j
public class MailService extends MailSender {
    @Value("${spring.mail.username}")
    private String fromEmail;
    private final JavaMailSender javaMailSender;

    public void sendSimpleMail(String to, String subject, String text) {
        log.info("📮 Sending simple mail to {}", to);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(fromEmail);

        javaMailSender.send(message);
        log.info("✅ Simple mail sent successfully!");
    }

    public void sendHtmlMail(String to, String subject, String htmlContent) {
        log.info("📮 Sending HTML mail to {}", to);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true → HTML 형식
            helper.setFrom("your_email@gmail.com");

            javaMailSender.send(mimeMessage);
            log.info("✅ HTML mail sent successfully!");
        } catch (MessagingException e) {
            log.error("❌ Failed to send HTML mail", e);
            throw new UAMailFailedException();
        }
    }

    @Override
    public void sendHtmlMailAsync(String to, String subject, String htmlContent) {
        super.sendHtmlMailAsync(to, subject, htmlContent);
    }

    @Override
    public void sendSimpleMailAsync(String to, String subject, String text) {
        super.sendSimpleMailAsync(to, subject, text);
    }
}
