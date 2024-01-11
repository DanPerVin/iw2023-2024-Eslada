package es.uca.iw.eslada.consultas;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import es.uca.iw.eslada.main.MainLayout;
import es.uca.iw.eslada.user.AuthenticatedUser;
import jakarta.annotation.security.RolesAllowed;

import java.util.UUID;


@Route(value = "consultation", layout = MainLayout.class)
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER", "ROLE_ATENCION"})
public class ConsultationView extends VerticalLayout {

    private ConsultationService consultationService;

    private ConsultationAdder consultationAdder;


    private AuthenticatedUser authenticatedUser;

    Checkbox checkbox;



    private final Grid<Consultation> grid = new Grid<>(Consultation.class,false);

    public ConsultationView(AuthenticatedUser authenticatedUser, ConsultationService consultationService,
                            ConsultationAdder consultationAdder) {

        this.authenticatedUser = authenticatedUser;
        this.consultationService = consultationService;
        this.consultationAdder = consultationAdder;

        checkbox = new Checkbox();
        checkbox.setLabel("Ver las consultas cerradas");
        checkbox.addValueChangeListener(e -> {
                if(checkbox.getValue())
                    grid.setItems(consultationService.findAll());
                else
                    grid.setItems(consultationService.findByClosed(false));
        });

        //Barra superior
        HorizontalLayout C1 = new HorizontalLayout();

        C1.setWidthFull();
        C1.setJustifyContentMode(JustifyContentMode.BETWEEN);


        //Titulo y boton de crear consulta
        C1.add(new H1("Consultas"));
        Button addNewConsultation = new Button("Nueva Consulta", e -> addConsultation());


        //Barra de busqueda no funcional (Opcional)
        /*if(authenticatedUser.get().get().getRoles().iterator().next().getName().matches("ADMIN")) {
        TextField searchField = new TextField();
        searchField.setWidth("50%");
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> {
            UUID id = UUID.fromString(e.getValue());
                if (!searchField.isEmpty())
                    grid.setItems(consultationService.findById(id).stream().toList());
                else grid.setItems(consultationService.findByClosed(false));
        });
        C1.add(searchField);
        }*/


        //Si eres usuario puedes crear consultas
        if (authenticatedUser.get().get().getRoles().iterator().next().getName().matches("USER"))
            C1.add(addNewConsultation);
        if (authenticatedUser.get().get().getRoles().iterator().next().getName().matches("ADMIN")
                || authenticatedUser.get().get().getRoles().iterator().next().getName().matches("ATENCION"))
            C1.add(checkbox);


        add(C1);

        //Creacion de las lineas del GRID, Si se es admin puedes cerrar las consultas
        grid.addColumn(Consultation::getId).setHeader("Id").setSortable(true);
        grid.addColumn(Consultation::getName).setHeader("Asunto");
        grid.addColumn(consultation -> consultation.getUser().getUsername()).setHeader("Usuario");
        grid.addColumn(Consultation::getCreationDate).setHeader("Fecha de creación").setSortable(true);
        grid.addColumn(Consultation::getClosed).setHeader("Cerrado").setSortable(true);
        if(authenticatedUser.get().get().getRoles().iterator().next().getName().matches("ADMIN")
                || authenticatedUser.get().get().getRoles().iterator().next().getName().matches("ATENCION")) {
            grid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, consultation) -> {
                Button closeConsultation = new Button("Cerrar", e -> this.closeConsultation(consultation));

                closeConsultation.setIcon(new Icon(VaadinIcon.TICKET));

                layout.add(closeConsultation);

            })).setHeader("Acciones");
        }

        //Evento para abrir el chat de la consulta
        grid.addItemDoubleClickListener(consultation -> UI.getCurrent().navigate("message/" + consultation.getItem().getId()));

        //Si eres admin ves las consultas abiertas, si eres user ves todas tus consultas
        if(authenticatedUser.get().get().getRoles().iterator().next().getName().matches("ADMIN")
                || authenticatedUser.get().get().getRoles().iterator().next().getName().matches("ATENCION"))
            if(checkbox.getValue())
                grid.setItems(consultationService.findAll());
            else
                grid.setItems(consultationService.findByClosed(false));
        if(authenticatedUser.get().get().getRoles().iterator().next().getName().matches("USER"))
            grid.setItems(consultationService.findByUser(authenticatedUser.get().get()));

        grid.setWidthFull();
        //grid.setHeightFull();

        add(grid);

    }

    //Funcion para crear consulta
    private void addConsultation() {
        Dialog newConsultationDiag = new Dialog();
        H2 headline = new H2("Nueva consulta");
        newConsultationDiag.add(headline);
        headline.getElement().getClassList().add("draggable");

        newConsultationDiag.add(consultationAdder);

        newConsultationDiag.setDraggable(true);
        newConsultationDiag.setResizable(true);

        newConsultationDiag.open();
    }

    //Funcion para cerrar consulta (NO SE BORRAN)
    private void closeConsultation(Consultation consultation) {
        Notification notification = new Notification("Has cerrado la consulta: " + consultation.getId());
        notification.setDuration(5000);

        Dialog dialog = new Dialog();
        H2 headline = new H2("Cerrar consulta");
        dialog.add(headline);
        headline.getElement().getClassList().add("draggable");
        Text message = new Text("¿ Seguro que quieres cerrar la consulta ?");
        dialog.add(message);

        Button cancelButton = new Button("Cancelar", e -> dialog.close());
        Button closeButton = new Button("Cerrar", e -> {
            consultationService.closeConsultation(consultation);
            dialog.close();
            notification.open();
            if(checkbox.getValue())
                grid.setItems(consultationService.findAll());
            else
                grid.setItems(consultationService.findByClosed(false));
        });

        closeButton.setThemeName("error");
        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, closeButton);
        dialog.add(buttonLayout);

        dialog.setDraggable(true);
        dialog.setResizable(true);

        dialog.open();
    }
}
