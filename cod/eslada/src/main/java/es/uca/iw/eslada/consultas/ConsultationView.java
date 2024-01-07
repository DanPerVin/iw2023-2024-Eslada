package es.uca.iw.eslada.consultas;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
public class ConsultationView extends VerticalLayout {

    private ConsultationService consultationService;


    private AuthenticatedUser authenticatedUser;


    private final Grid<Consultation> grid = new Grid<>(Consultation.class,false);

    public ConsultationView(AuthenticatedUser authenticatedUser, ConsultationService consultationService) {

        this.authenticatedUser = authenticatedUser;
        this.consultationService = consultationService;

        HorizontalLayout C1 = new HorizontalLayout();

        C1.setWidthFull();
        C1.setJustifyContentMode(JustifyContentMode.BETWEEN);


        C1.add(new H1("Consultas"));

        Button addNewConsultation = new Button("Nueva Consulta", e -> addConsultation());

        /*if(authenticatedUser.get().get().getRoles().iterator().next().getName().matches("ADMIN")) {
        TextField searchField = new TextField();
        searchField.setWidth("50%");
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> {   //TODO: Morir
            UUID id = UUID.fromString(e.getValue());
                if (!searchField.isEmpty())
                    grid.setItems(consultationService.findById(id).stream().toList());
                else grid.setItems(consultationService.findByClosed(false));
        });
        C1.add(searchField);
        }*/


        if(authenticatedUser.get().get().getRoles().iterator().next().getName().matches("USER"))
            C1.add(addNewConsultation);


        add(C1);

        grid.addColumn(Consultation::getId).setHeader("Id").setSortable(true);
        grid.addColumn(Consultation::getName).setHeader("Asunto");
        grid.addColumn(consultation -> consultation.getUser().getUsername()).setHeader("Usuario");
        grid.addColumn(Consultation::getCreationDate).setHeader("Fecha de creación").setSortable(true);
        grid.addColumn(Consultation::getClosed).setHeader("Cerrado");
        //grid.addColumn(consultation -> consultation.getFirstMessage().getMessageString()).setHeader("Primer mensaje");
        grid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, consultation) -> {

            //Buttons (openChat as doubleclickListener) TODO: Revisar esto
            //Button openChatConsultation = new Button("Abrir consulta", e -> this.closeConsultation(consultation)); //TODO: Cambiar evento
            Button closeConsultation = new Button("Cerrar", e -> this.closeConsultation(consultation));

            //Icons
            //openChatConsultation.setIcon(new Icon(VaadinIcon.TICKET));
            closeConsultation.setIcon(new Icon(VaadinIcon.TICKET));

            //Add Buttons depending on user
            //layout.add(openChatConsultation);
            if(authenticatedUser.get().get().getRoles().iterator().next().getName().matches("ADMIN"))
                layout.add(closeConsultation);

        })).setHeader("Acciones");

        grid.addItemDoubleClickListener(consultation -> UI.getCurrent().navigate("message/" + consultation.getItem().getId()));

        if(authenticatedUser.get().get().getRoles().iterator().next().getName().matches("ADMIN"))
            grid.setItems(consultationService.findByClosed(false));
        if(authenticatedUser.get().get().getRoles().iterator().next().getName().matches("USER"))
            grid.setItems(consultationService.findByUser(authenticatedUser.get().get()));

        add(grid);

    }

    private void addConsultation() {
        //TODO: Añadir consulta
        Dialog newConsultationDiag = new Dialog();
        H2 headline = new H2("Nueva consulta");
        newConsultationDiag.add(headline);
        headline.getElement().getClassList().add("draggable");

        newConsultationDiag.setDraggable(true);
        newConsultationDiag.setResizable(true);

        newConsultationDiag.open();
    }

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
            consultationService.findByClosed(false);
            dialog.close();
            notification.open();
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
