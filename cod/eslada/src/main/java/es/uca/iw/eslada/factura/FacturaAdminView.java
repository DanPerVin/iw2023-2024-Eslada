package es.uca.iw.eslada.factura;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import es.uca.iw.eslada.contrato.Contrato;
import es.uca.iw.eslada.contrato.ContratoService;
import es.uca.iw.eslada.main.MainLayout;
import es.uca.iw.eslada.servicio.Servicio;
import es.uca.iw.eslada.user.AuthenticatedUser;
import jakarta.annotation.security.RolesAllowed;

import java.io.ByteArrayInputStream;
import java.util.Collection;


@Route(value = "factura_ADMIN", layout = MainLayout.class)
@RolesAllowed({"ROLE_ADMIN","ROLE_FACTURAS"})

public class FacturaAdminView extends VerticalLayout {

    private final AuthenticatedUser authenticatedUser;

    private final FacturaPdfGenerator facturaPdfGenerator;

    private final FacturaService facturaService;

    private final ContratoService contratoService;

    private final Grid<Factura> grid = new Grid<>(Factura.class, false);


    public FacturaAdminView(AuthenticatedUser authenticatedUser, FacturaService facturaService, FacturaPdfGenerator facturaPdfGenerator, ContratoService contratoService){
        this.authenticatedUser = authenticatedUser;
        this.facturaService = facturaService;
        this.facturaPdfGenerator = facturaPdfGenerator;
        this.contratoService = contratoService;

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        H1 title = new H1("Facturas");

        headerLayout.add(title);

        add(headerLayout);

        grid.addColumn(Factura::getFecha).setHeader("Fecha").setSortable(true).setResizable(true).setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(factura -> {
            Contrato contrato = factura.getContrato();
            Collection<Servicio> servicios = contrato.getServicios();

            double totalPrice = 0;
            for (Servicio servicio : servicios) {
                totalPrice += servicio.getPrice();
            }

            return totalPrice;
        }).setHeader("Importe").setSortable(true).setResizable(true).setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, factura) -> {
            Button mostrarDetalles = new Button("Mostrar Detalles", e -> this.detallesFactura(factura));
            mostrarDetalles.setIcon(new Icon(VaadinIcon.PLUS));
            layout.add(mostrarDetalles);

            Button downloadButton = new Button("Descargar", e -> {
                byte[] pdfBytes = facturaPdfGenerator.downloadFactura(factura);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
                StreamResource res = new StreamResource("file.pdf", () -> inputStream);
                Anchor a = new Anchor(res, "Click aquí para descargar");
                add(a);
            });
            downloadButton.setIcon(new Icon(VaadinIcon.CLOUD_DOWNLOAD_O));
            layout.add(downloadButton);

            Button emailButton = new Button("Enviar", e -> {
                facturaPdfGenerator.sendFacturaEmail(factura.getContrato().getEmail(), factura);
            });
            emailButton.setIcon(new Icon(VaadinIcon.ENVELOPE));
            layout.add(emailButton);
        })).setHeader("Acciones").setResizable(true).setAutoWidth(true).setFlexGrow(0);


        grid.setItems(facturaService.findAll());

        add(grid);


    }

    private void detallesFactura(Factura factura) {
        Dialog dialog = new Dialog();

        Contrato contrato = factura.getContrato();

        H2 headline = new H2("Detalles de la Factura");
        VerticalLayout verticalLayout = new VerticalLayout();
        Grid<Servicio> detallesgrid = new Grid<>(Servicio.class,false);
        Collection<Servicio> servicios = contrato.getServicios();
        FormLayout formLayout = new FormLayout();

        TextField nombreField = new TextField("Nombre");
        TextField apellidosField = new TextField("Apellidos");
        TextField emailField = new TextField("Email");
        TextField dniField = new TextField("DNI");
        TextField direccionField = new TextField("Dirección");
        TextField ibanField = new TextField("IBAN");
        TextField fechaField = new TextField("Fecha");

        nombreField.setValue(contrato.getNombre());
        apellidosField.setValue(contrato.getApellidos());
        emailField.setValue(contrato.getEmail());
        dniField.setValue(contrato.getDni());
        direccionField.setValue(contrato.getDireccion());
        ibanField.setValue(contrato.getIban());
        fechaField.setValue(contrato.getFecha().toString());

        nombreField.setReadOnly(true);
        apellidosField.setReadOnly(true);
        emailField.setReadOnly(true);
        dniField.setReadOnly(true);
        direccionField.setReadOnly(true);
        ibanField.setReadOnly(true);
        fechaField.setReadOnly(true);

        formLayout.add(nombreField, apellidosField, emailField, dniField, direccionField, ibanField, fechaField);

        verticalLayout.add(formLayout);
        verticalLayout.add(new H2("Servicios contratados"));

        detallesgrid.addColumn(Servicio::getName).setHeader("Nombre");
        detallesgrid.addColumn(Servicio::getPrice).setHeader("Precio");
        detallesgrid.setItems(servicios);

        verticalLayout.add(detallesgrid);
        verticalLayout.add(new H4("Precio Total: "+ contratoService.getServiciosPrecio(contrato)+" €"));

        Button closeButton = new Button("Cerrar", e -> dialog.close());

        dialog.add(headline, verticalLayout, closeButton);
        dialog.open();

    }

}
