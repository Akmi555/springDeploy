package org.blb.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserMailSender {

    private final JavaMailSender javaMailSender;

    public void send(String email, String subject, String text) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8"); // создали некую "обертку" из spring "для удобства"

        try {
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text, true);
        } catch (MessagingException e) {
            throw new IllegalStateException(e);
        }

        javaMailSender.send(message);

    }
    private String codeUserId(String id){
        String code=randomString(5);
        StringBuilder result= new StringBuilder();
        for (int i=0;i<code.length();i++){
            result.append(code.charAt(i));
            if(i==1) {
                result.append(numberToCodeString(id));
            }
        }
        return result.toString();
    }
    private String numberToCodeString(String number){
        byte[] byteArray=number.getBytes();
        StringBuilder buffer =new StringBuilder();;
        for(int i=0;i<byteArray.length;i++){
            buffer.append((char)(byteArray[i]+49));
        }
        return buffer.toString();
    }

    private String randomString(int length){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 106; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}
