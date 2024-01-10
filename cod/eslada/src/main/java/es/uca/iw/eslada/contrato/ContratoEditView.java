package es.uca.iw.eslada.contrato;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
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
import es.uca.iw.eslada.main.MainLayout;
import es.uca.iw.eslada.servicio.Servicio;
import jakarta.annotation.security.RolesAllowed;

import java.util.Collection;

@Route(value = "contrato_edit", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
public class ContratoEditView extends VerticalLayout {

    private final ContratoService contratoService;
    private final Grid<Contrato> grid = new Grid<>(Contrato.class, false);

    private final ContratoEditor contratoEditor;


    public ContratoEditView(ContratoService contratoService, ContratoEditor contratoEditor) {
        this.contratoService = contratoService;

        this.contratoEditor = contratoEditor;

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        H1 title = new H1("Contratos");


        headerLayout.add(title);
        add(headerLayout);

        grid.addColumn(Contrato::getNombre).setHeader("Nombre").setSortable(true).setResizable(true).setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(Contrato::getApellidos).setHeader("Apellidos").setSortable(true).setResizable(true).setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(contrato -> contratoService.getServiciosNames(contrato)).setHeader("Servicios").setSortable(true).setResizable(true).setAutoWidth(true).setFlexGrow(0);

        grid.addColumn(Contrato::getFecha).setHeader("Fecha de contratación").setSortable(true).setResizable(true).setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, contrato) -> {
            Button detailButton = new Button("Mostrar Detalles", e -> this.detallesContrato(contrato));
            detailButton.setIcon(new Icon(VaadinIcon.PLUS));
            layout.add(detailButton);

            Button editButton = new Button("Edit", e -> this.editContrato(contrato));
            editButton.setIcon(new Icon(VaadinIcon.EDIT));
            layout.add(editButton);

            Button deleteButton = new Button("Delete", e -> this.deleteContrato(contrato));
            deleteButton.setIcon(new Icon(VaadinIcon.TRASH));
            layout.add(deleteButton);
        })).setHeader("Acciones");

        grid.setItems(contratoService.findAll());

        add(grid);
    }

    private void detallesContrato(Contrato contrato) {
        Dialog dialog = new Dialog();

        H2 headline = new H2("Detalles del Contrato");
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

    private void editContrato(Contrato contrato) {
        contratoEditor.editContrato(contrato);
        contratoEditor.setCallback(() -> {
            grid.setItems(contratoService.findAll());
        });

        Dialog dialog = new Dialog();
        H2 headline = new H2("Edit Contrato");
        dialog.add(headline);
        headline.getElement().getClassList().add("draggable");

        dialog.add(contratoEditor);

        dialog.setDraggable(true);
        dialog.setResizable(true);

        dialog.open();
        dialog.addDialogCloseActionListener(e-> {
            grid.setItems(contratoService.findAll());
            dialog.close();
        });
    }



    private void deleteContrato(Contrato contrato) {
        Dialog dialog = new Dialog();
        H2 headline = new H2("Delete Contrato");
        dialog.add(headline);
        headline.getElement().getClassList().add("draggable");
        Text message = new Text("¿ Seguro que quieres borrar la contrato ?");
        dialog.add(message);

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        Button deleteButton = new Button("Delete", e -> {
            contratoService.delete(contrato);
            grid.setItems(contratoService.findAll());
            dialog.close();
        });

        deleteButton.setThemeName("error");
        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, deleteButton);
        dialog.add(buttonLayout);

        dialog.setDraggable(true);
        dialog.setResizable(true);

        dialog.open();
    }

}

