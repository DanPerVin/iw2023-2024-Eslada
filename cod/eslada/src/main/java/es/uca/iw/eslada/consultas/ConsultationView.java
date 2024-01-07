package es.uca.iw.eslada.consultas;

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
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import es.uca.iw.eslada.main.MainLayout;
import es.uca.iw.eslada.user.AuthenticatedUser;
import jakarta.annotation.security.RolesAllowed;


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

        if(authenticatedUser.get().get().getRoles().iterator().next().getName().matches("USER"))
            C1.add(addNewConsultation);

        add(C1);
        
        grid.addColumn(Consultation::getId).setHeader("Id").setSortable(true);
        grid.addColumn(Consultation::getClosed).setHeader("Cerrado").setSortable(true);
        grid.addColumn(consultation -> consultation.getUser().getUsername()).setHeader("Usuario").setSortable(true);
        grid.addColumn(Consultation::getCreationDate).setHeader("Fecha de creación").setSortable(true);
        //grid.addColumn(consultation -> consultation.getFirstMessage().getMessageString()).setHeader("Primer mensaje");
        if(authenticatedUser.get().get().getRoles().iterator().next().getName().matches("ADMIN")) {
            grid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, consultation) -> {
                Button closeConsultation = new Button("Close", e -> this.closeConsultation(consultation));
                closeConsultation.setIcon(new Icon(VaadinIcon.TICKET));
                layout.add(closeConsultation);
            })).setHeader("Acciones");
        }


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
        notification.open();
        //TODO: Cerrar consultas

    }
}
