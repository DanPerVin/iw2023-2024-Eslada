package es.uca.iw.eslada.contrato;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import es.uca.iw.eslada.main.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import java.util.stream.Stream;

@Route(value = "contrato_lista", layout = MainLayout.class)
@RolesAllowed("ROLE_USER")
public class ContratoListView extends VerticalLayout {
    private final ContratoService contratoService;
    private final Grid<Contrato> grid = new Grid<>(Contrato.class, false);
    private final ContratoAdder contratoAdder;

    public ContratoListView(ContratoService contratoService,ContratoAdder contratoAdder) {
        this.contratoService = contratoService;
        this.contratoAdder = contratoAdder;

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        H1 title = new H1("Contratos");
        Button addButton = new Button("Contratar", e -> addContrato());

        headerLayout.add(title, addButton);
        add(headerLayout);

        grid.addColumn(Contrato::getFecha).setHeader("Fecha de contratación").setSortable(true);
        grid.addColumn(contrato -> contratoService.getServiciosNames(contrato)).setHeader("Servicios").setSortable(true);

        grid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, contrato) -> {
            Button editButton = new Button("Mostrar Detalles", e -> this.detallesContrato(contrato));
            editButton.setIcon(new Icon(VaadinIcon.PLUS));
            layout.add(editButton);
        })).setHeader("Acciones");

        grid.setItems(contratoService.findAll());

        add(grid);
    }

    private void detallesContrato(Contrato contrato) {
        Dialog dialog = new Dialog();

        H2 headline = new H2("Detalles del Contrato");
        FormLayout formLayout = new FormLayout();

        TextField nombreField = new TextField("Nombre");
        TextField apellidosField = new TextField("Apellidos");
        TextField emailField = new TextField("Email");
        TextField dniField = new TextField("DNI");
        TextField direccionField = new TextField("Dirección");
        TextField ibanField = new TextField("IBAN");
        TextField fechaField = new TextField("Fecha");
        TextField serviciosField = new TextField("Servicios", contratoService.getServiciosNames(contrato));

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

        formLayout.add(nombreField, apellidosField, emailField, dniField, direccionField, ibanField, fechaField, serviciosField);

        Button closeButton = new Button("Cerrar", e -> dialog.close());

        dialog.add(headline, formLayout, closeButton);
        dialog.open();


    }

    private void addContrato() {
        contratoAdder.setCallback(() -> {
            grid.setItems(contratoService.findAll());
        });
        Dialog dialog = new Dialog();
        H2 headline = new H2("Add Contrato");
        dialog.add(headline);
        headline.getElement().getClassList().add("draggable");

        dialog.add(contratoAdder);

        dialog.setDraggable(true);
        dialog.setResizable(true);

        dialog.open();
        dialog.addDialogCloseActionListener(e-> {
            grid.setItems(contratoService.findAll());
            dialog.close();
        });
    }


}
