package es.uca.iw.eslada.contrato;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import es.uca.iw.eslada.main.MainLayout;
import es.uca.iw.eslada.servicio.Servicio;
import es.uca.iw.eslada.servicio.ServicioType;
import es.uca.iw.eslada.user.AuthenticatedUser;
import es.uca.iw.eslada.user.User;
import jakarta.annotation.security.RolesAllowed;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Route(value = "contrato_lista", layout = MainLayout.class)
@RolesAllowed("ROLE_USER")
public class ContratoListView extends VerticalLayout {
    private final AuthenticatedUser authenticatedUser;
    private final ContratoService contratoService;
    private final Grid<Contrato> grid = new Grid<>(Contrato.class, false);
    private final ContratoAdder contratoAdder;
    private final Map<ServicioType, MultiSelectComboBox<Servicio>> comboboxes = new HashMap<>();
    private Optional<User> user = Optional.of(new User());
    private final List<Contrato> contratos;



    public ContratoListView(ContratoService contratoService,ContratoAdder contratoAdder, AuthenticatedUser authenticatedUser) {
        this.contratoService = contratoService;
        this.contratoAdder = contratoAdder;
        this.authenticatedUser = authenticatedUser;

        user = authenticatedUser.get();
        contratos = user.get().getContratos();


        buildUI();
    }

    private void buildUI() {
        H1 title = new H1("Contratos");
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        Button addButton = new Button("Contratar", VaadinIcon.PLUS.create(), e -> addContrato());

        headerLayout.add(title, addButton);
        add(headerLayout);

        if(!user.get().getContratos().isEmpty()){
        grid.setItems(contratos);
        grid.addColumn(Contrato::getFecha).setHeader("Fecha").setSortable(true);
        grid.addColumn(Contrato::getServicios).setHeader("Servicios Contratados");
        grid.addColumn(contrato -> {
            double totalPrice = 0;
            List<Servicio> servicios = (List<Servicio>) contrato.getServicios();

            if (servicios != null) {
                for (Servicio servicio : servicios) {
                    totalPrice += servicio.getPrice();
                }
            }   return totalPrice;
        }).setHeader("Precio").setAutoWidth(true);

        grid.addColumn(createToggleDetailsRenderer(grid));

        grid.setDetailsVisibleOnClick(false);
        grid.setItemDetailsRenderer(createContratoDetailsRenderer());

        add(grid);
        }else{ add("No tiene contratos disponibles");}
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
        private final TextField servicioField = new TextField("Servicio");
        private final TextField precioField = new TextField("Precio");
        private final TextField nombreField = new TextField("Nombre");
        private final TextField apellidosField = new TextField("Apellidos");
        private final TextField emailField = new TextField("Email");
        private final TextField dniField = new TextField("D.N.I.");
        private final TextField direccionField = new TextField("Dirección");
        private final TextField ibanField = new TextField("Cuenta Bancaria");

        public ContratoDetailsFormLayout() {
            Stream.of(servicioField, precioField, nombreField, apellidosField, emailField, dniField, direccionField, ibanField).forEach(field -> {
                field.setReadOnly(true);
                add(field);
            });

            setResponsiveSteps(new ResponsiveStep("0", 2));
            setColspan(emailField, 2);
            setColspan(direccionField, 2);
            setColspan(ibanField, 2);
        }

        public void setContrato(Contrato contrato) {
            for(Servicio servicio : contrato.getServicios()){servicioField.setValue(servicio.getName());}
            //TODO: Mostrar todos los servicios
            for(Servicio servicio : contrato.getServicios()){precioField.setValue(String.valueOf(servicio.getPrice()));}
            emailField.setValue(contrato.getEmail());
            nombreField.setValue(contrato.getNombre());
            apellidosField.setValue(contrato.getApellidos());
            dniField.setValue(contrato.getDni());
            direccionField.setValue(contrato.getDireccion());
            ibanField.setValue(contrato.getIban());
        }
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
