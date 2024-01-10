package es.uca.iw.eslada.contrato;

import com.vaadin.flow.component.Text;
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

@Route(value = "contrato_edit", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
public class ContratoEditView extends VerticalLayout {
    private final ContratoService contratoService;
    private final ContratoEditor contratoEditor;
    private final ContratoAdder contratoAdder;
    private final Grid<Contrato> grid = new Grid<>(Contrato.class, false);

    public ContratoEditView(ContratoService contratoService, ContratoEditor contratoEditor, ContratoAdder contratoAdder) {
        this.contratoService = contratoService;
        this.contratoAdder = contratoAdder;
        this.contratoEditor = contratoEditor;

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        H1 title = new H1("Contratos");
        Button addButton = new Button("Add", e -> addContrato());

        headerLayout.add(title, addButton);

        add(headerLayout);

        buildUI();
    }

    private void buildUI() {
        grid.addColumn(Contrato::getNombre).setHeader("Nombre");
        grid.addColumn(Contrato::getApellidos).setHeader("Apellidos");
        grid.addColumn(Contrato::getNombre).setHeader("Fecha").setSortable(true);


        grid.addColumn(createToggleDetailsRenderer(grid));
        grid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, contrato) -> {
            Button editButton = new Button("Edit", e -> this.editContrato(contrato));
            editButton.setIcon(new Icon(VaadinIcon.EDIT));
            layout.add(editButton);

            Button deleteButton = new Button("Delete", e -> this.deleteContrato(contrato));
            deleteButton.setIcon(new Icon(VaadinIcon.TRASH));
            layout.add(deleteButton);
        })).setHeader("Acciones");

        grid.setDetailsVisibleOnClick(false);
        grid.setItemDetailsRenderer(createContratoDetailsRenderer());

        grid.setItems(contratoService.findAll());

        add(grid);
    }

    private static Renderer<Contrato> createToggleDetailsRenderer(
            Grid<Contrato> grid) {
        return LitRenderer.<Contrato> of(
                        "<vaadin-button theme=\"tertiary\" @click=\"${handleClick}\">Detalles</vaadin-button>")
                .withFunction("handleClick",
                        contrato -> grid.setDetailsVisible(contrato,
                                !grid.isDetailsVisible(contrato)));
    }

    private static ComponentRenderer<ContratoDetailsFormLayout, Contrato> createContratoDetailsRenderer() {
        return new ComponentRenderer<>(ContratoDetailsFormLayout::new,
                ContratoDetailsFormLayout::setContrato);
    }

    private static class ContratoDetailsFormLayout extends FormLayout {
        private final TextField emailField = new TextField("Email");
        private final TextField dniField = new TextField("D.N.I.");
        private final TextField direccionField = new TextField("Dirección");
        private final TextField ibanField = new TextField("Cuenta Bancaria");

        public ContratoDetailsFormLayout() {
            Stream.of(emailField, dniField, direccionField, ibanField).forEach(field -> {
                field.setReadOnly(true);
                add(field);
            });

            setResponsiveSteps(new ResponsiveStep("0", 3));
            setColspan(emailField, 3);
            setColspan(dniField, 3);
            setColspan(direccionField, 3);
            setColspan(ibanField, 3);
        }

        public void setContrato(Contrato contrato) {
            emailField.setValue(contrato.getEmail());
            dniField.setValue(contrato.getDni());
            direccionField.setValue(contrato.getDireccion());
            ibanField.setValue(contrato.getIban());
        }
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

