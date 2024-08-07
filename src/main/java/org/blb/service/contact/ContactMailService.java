package org.blb.service.contact;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.blb.DTO.contactDTO.ContactDTO;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactMailService {
    private final JavaMailSender mailSender;

    public void sendMail(ContactDTO dto){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setTo("Cohort40.1.11@gmail.com");
            mimeMessageHelper.setReplyTo(dto.getContactEmail());
            mimeMessageHelper.setSubject("Contact from "+ dto.getContactName() + " | Email: "+dto.getContactEmail());
            mimeMessageHelper.setText(dto.getContactMessage(), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email", e);
        }
    }
}
