package com.libitum.app.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String ourEmailSender;

    public void sendVerificationEmail(String to, String token){
        String subject = "Verifica tu cuenta";
        StringBuilder tokenHtml = new StringBuilder();
        for (char c : token.toCharArray()) {
            tokenHtml.append("<span style=\"display:inline-block; margin:5px; padding:10px 15px; "
                            + "background:#007bff; color:white; font-size:24px; font-weight:bold; "
                            + "border-radius:5px;\">")
                    .append(c)
                    .append("</span>");
        }

        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="UTF-8">
              <title>Verifica tu cuenta</title>
            </head>
            <body style="font-family: Arial, sans-serif; background-color:#f4f4f4; padding:20px;">
              <table align="center" width="600" cellpadding="0" cellspacing="0" style="background:white; border-radius:8px; padding:20px;">
                <tr>
                  <td align="center">
                    <img src="cdi:logoImage" alt="Logo" width="120" style="margin-bottom:20px;" />
                  </td>
                </tr>
                <tr>
                  <td>
                    <h2 style="color:#333;">¡Bienvenido a Libitum!</h2>
                    <p style="color:#555;">
                      Gracias por registrarte. Copia y pega el código de abajo para verificar tu cuenta:
                    </p>
                    <p style="text-align:center;">
                     %s
                    </p>
                    <p style="color:#999; font-size:12px;">
                      Si no creaste esta cuenta, simplemente ignora este correo.
                    </p>
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """.formatted(tokenHtml);
        ClassPathResource img = new ClassPathResource("images/spot-commercial-logo-libitum.png");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(ourEmailSender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.addInline("logoImage",img);
        } catch (MessagingException e) {
            throw new RuntimeException("Error en email sender (MimeMessageHelper) EmailService: "+e);
        }


        javaMailSender.send(message);
    }

    /*
    public void emailSender()
    HACER UN MAIL SENDER, PARA QUE SE PUEDA USAR CON TODOS LOS TIPOS DE USERS QUE TENEMOS TANTO LOGIN REGISTER COMO USER NORMAL
    ASÍ QUE HAY QUE HACER UN USER ABSTRACTO PARA QUE PUEDA HABER POLIMORFISMO

    Y ASÍ HACER ESTE EMAIL SERVICE MÁS ÚTIL Y NO DUPLICAR CÓDIGO
     */
}
