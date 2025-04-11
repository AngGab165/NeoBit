package com.neobit.sugerencia.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class CorreoUtil {

    public static void enviarCorreo(String destinatario, String asunto, String contenido) {
        final String remitente = "neobit680@gmail.com";
        final String contrasena = "duwt uzvb vwcv wwfi"; // contraseña de aplicación

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true"); // Activamos SSL directamente
        props.put("mail.smtp.starttls.enable", "false"); // Desactivamos STARTTLS
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465"); // Puerto SSL

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, contrasena);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText(contenido);

            Transport.send(message);
            System.out.println("Correo enviado correctamente.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
