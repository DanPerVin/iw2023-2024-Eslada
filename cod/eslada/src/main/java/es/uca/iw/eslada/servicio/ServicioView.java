package es.uca.iw.eslada.servicio;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import es.uca.iw.eslada.main.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;

@Route(value = "servicios", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
public class ServicioView extends VerticalLayout {
    private final ServicioService servicioService;
    private final ServicioEditor servicioEditor;
    private final ServicioAdder servicioAdder;

    private final Grid<Servicio> grid = new Grid<>(Servicio.class,false);


    public ServicioView(ServicioService servicioService, ServicioEditor servicioEditor, ServicioAdder servicioAdder){
        this.servicioService = servicioService;
        this.servicioEditor = servicioEditor;
        this.servicioAdder = servicioAdder;


        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        H1 title = new H1("Servicios");
        Button addButton = new Button("Add", e -> addServicio());

        headerLayout.add(title, addButton);

        add(headerLayout);

        //add(new H1("Servicios"));

        grid.addColumn(Servicio::getName).setHeader("Nombre");
        grid.addColumn(new ComponentRenderer<>(Button::new, (button,servicio)-> {
            button.addClickListener(e ->this.editServicio(servicio)); button.setIcon(new Icon(VaadinIcon.EDIT));})).setHeader("Acciones");

        grid.setItems(servicioService.findAll());

        add(grid);
    }

    private void editServicio(Servicio servicio) {
        servicioEditor.editServicio(servicio);

        Dialog dialog = new Dialog();
        H2 headline = new H2("Add Servicio");
        dialog.add(headline);
        headline.getElement().getClassList().add("draggable");

        dialog.add(servicioEditor);

        dialog.setDraggable(true);
        dialog.setResizable(true);

        dialog.open();
        dialog.addDialogCloseActionListener(e-> {
            grid.setItems(servicioService.findAll());
            dialog.close();
        });
    }

    private void addServicio() {

        Dialog dialog = new Dialog();
        H2 headline = new H2("Add Servicio");
        dialog.add(headline);
        headline.getElement().getClassList().add("draggable");

        dialog.add(servicioAdder);

        dialog.setDraggable(true);
        dialog.setResizable(true);

        dialog.open();
        dialog.addDialogCloseActionListener(e-> {
            grid.setItems(servicioService.findAll());
            dialog.close();
        });
    }
}
