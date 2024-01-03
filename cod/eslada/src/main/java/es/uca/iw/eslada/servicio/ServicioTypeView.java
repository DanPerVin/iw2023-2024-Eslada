package es.uca.iw.eslada.servicio;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import es.uca.iw.eslada.main.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "serviciostype", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
public class ServicioTypeView extends VerticalLayout {
    private final ServicioService servicioService;
    private final Grid<ServicioType> grid = new Grid<>(ServicioType.class,false);
    public ServicioTypeView(ServicioService servicioService){
        this.servicioService = servicioService;

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        H1 title = new H1("ServicioTypes");
        Button addButton = new Button("Add", e -> addServicioType());

        headerLayout.add(title, addButton);

        add(headerLayout);

        grid.addColumn(ServicioType::getName).setHeader("Nombre").setSortable(true);

        grid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, servicioType) -> {
            Button editButton = new Button("Edit", e -> this.editServicioType(servicioType));
            editButton.setIcon(new Icon(VaadinIcon.EDIT));
            layout.add(editButton);

            Button deleteButton = new Button("Delete", e -> this.deleteServicioType(servicioType));
            deleteButton.setIcon(new Icon(VaadinIcon.TRASH));
            layout.add(deleteButton);
        })).setHeader("Acciones");

        grid.setItems(servicioService.findAllTypes());

        add(grid);

    }

    private void deleteServicioType(ServicioType servicioType) {
    }

    private void editServicioType(ServicioType servicioType) {
    }

    private void addServicioType() {
    }
}
