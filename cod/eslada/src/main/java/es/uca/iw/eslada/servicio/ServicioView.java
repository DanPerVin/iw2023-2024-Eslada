package es.uca.iw.eslada.servicio;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
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

    private final Grid<Servicio> grid = new Grid<>(Servicio.class,false);

    public ServicioView(ServicioService servicioService, ServicioEditor servicioEditor){
        this.servicioService = servicioService;
        this.servicioEditor = servicioEditor;

        add(new H1("Servicios"));

        grid.addColumn(Servicio::getName).setHeader("Nombre");
        grid.addColumn(new ComponentRenderer<>(Button::new, (button,servicio)-> {
            button.addClickListener(e ->this.editServicio(servicio)); button.setIcon(new Icon(VaadinIcon.EDIT));})).setHeader("Acciones");

        grid.setItems(servicioService.findAll());

        add(grid);
    }

    private void editServicio(Servicio servicio) {
        servicioEditor.editServicio(servicio);
        add(servicioEditor);
    }
}
