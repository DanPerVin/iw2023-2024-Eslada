package es.uca.iw.eslada.factura;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import es.uca.iw.eslada.contrato.Contrato;
import es.uca.iw.eslada.servicio.Servicio;
import es.uca.iw.eslada.user.AuthenticatedUser;
import es.uca.iw.eslada.user.User;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Optional;

@SpringComponent
@UIScope
public class FacturaPdfGenerator extends VerticalLayout {
    AuthenticatedUser authenticatedUser;

    private EmailService emailService;
    private static Optional<User> user = Optional.of(new User());

    public FacturaPdfGenerator(AuthenticatedUser authenticatedUser, EmailService emailService){
        this.authenticatedUser = authenticatedUser;
        this.emailService = emailService;

    user = authenticatedUser.get();
    }

    public byte[] downloadFactura(Factura factura) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            document.add(new Paragraph("Factura"));
            document.add(new Paragraph("Fecha:" + factura.getFecha()));
            document.add(new Paragraph("Cliente: " + user.get().getName() + " " + user.get().getSurname()));
            document.add(new Paragraph("con D.N.I. " + user.get().getDni()));
            document.add(new Paragraph("Correo electrónico: " + user.get().getEmail()));
            document.add(new Paragraph("Cuenta de cobro: " + factura.getContrato().getIban()));
            document.add(new Paragraph("Identificador del contrato: " + factura.getContrato().getId()));
            document.add(new Paragraph("Detalle de la Factura:"));
            for(Servicio servicio: factura.getContrato().getServicios()){
                document.add(new Paragraph(servicio.getServicioType().getName() + " " + servicio.getName() + " " + servicio.getPrice()));
            }
            Contrato contrato = new Contrato();
            contrato = factura.getContrato();
            Collection<Servicio> servicios = contrato.getServicios();

            double totalPrice = 0;
            for (Servicio servicio : servicios) {
                totalPrice += servicio.getPrice();
            }
            document.add(new Paragraph("Precio total:" + totalPrice));

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return byteArrayOutputStream.toByteArray();
    }

    public void sendFacturaEmail(String email, Factura factura) {
        String subject = "Gracias por confiar en EsLaDa";

        FacturaPdfGenerator facturaPdfGenerator = new FacturaPdfGenerator(authenticatedUser, emailService);
        emailService.sendSimpleMessage(email, subject, factura, facturaPdfGenerator);
    }
}