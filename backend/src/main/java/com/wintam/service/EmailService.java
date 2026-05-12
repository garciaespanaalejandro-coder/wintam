package com.wintam.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String email, String code){
        SimpleMailMessage message= new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Wintam- Verifica tu cuenta");
        message.setText("Tu código de verificación es: "+code+
                "\n\n Introdúcelo en la app para activar tu cuenta." +
                "\n Este código expirará en 24 horas.");
        mailSender.send(message);
    }

    public void sendRecoverPassword(String email, String code){
        SimpleMailMessage message= new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Wintam- Recupera tu contraseña");
        message.setText("Tu código para recuperar la contraseña es: "+code+
                "\n\n Introdúcelo en la app para cambiar tu contraseña."+
                "\n Este código expirará en 24 horas.");
        mailSender.send(message);
    }

    public void sendAdvice(String email){
        SimpleMailMessage message= new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Wintam- Aviso de conducta");
        message.setText("Has recibido un aviso por conducta inadecuada en Wintam.\n\n" +
                "Nuestro equipo ha revisado un reporte sobre tu comportamiento y " +
                "queremos recordarte que debes respetar las normas de la comunidad.\n\n" +
                "Reincidencias pueden resultar en penalizaciones de Karma o el baneo permanente de tu cuenta.\n\n" +
                "El equipo de Wintam.");
        mailSender.send(message);
    }
}
