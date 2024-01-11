package es.uca.iw.eslada.factura;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class EmailService {
    private JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(String to, String subject, Factura factura, FacturaPdfGenerator facturaPdfGenerator) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mail@esperanza.com");
        message.setTo("esperanza.canocanalejas@alum.uca.es");
        message.setSubject(subject);


        byte[] pdfBytes = facturaPdfGenerator.downloadFactura(factura);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
        StreamResource res = new StreamResource("file.pdf", () -> inputStream);
        Anchor a = new Anchor(res, "Click aquí para descargar");
        message.setText(String.valueOf(a));
        try{
            emailSender.send(message);
        } catch (MailParseException e) {
            e.printStackTrace();
        }
    }
}
