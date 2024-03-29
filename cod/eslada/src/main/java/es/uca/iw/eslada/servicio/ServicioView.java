package es.uca.iw.eslada.servicio;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import es.uca.iw.eslada.main.MainLayout;
import jakarta.annotation.security.RolesAllowed;



@Route(value = "servicios", layout = MainLayout.class)
@RolesAllowed({"ROLE_ADMIN","ROLE_MARKETING"})
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

        grid.addColumn(Servicio::getName).setHeader("Nombre").setSortable(true);;
        grid.addColumn(Servicio::getPrice).setHeader("Precio").setSortable(true);;
        grid.addColumn(servicio -> servicio.getServicioType().getName()).setHeader("Tipo").setSortable(true);

        grid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, servicio) -> {
            Button editButton = new Button("Edit", e -> this.editServicio(servicio));
            editButton.setIcon(new Icon(VaadinIcon.EDIT));
            layout.add(editButton);

            Button deleteButton = new Button("Delete", e -> this.deleteServicio(servicio));
            deleteButton.setIcon(new Icon(VaadinIcon.TRASH));
            layout.add(deleteButton);
        })).setHeader("Acciones");

        grid.setItemDetailsRenderer(createServicioDetailsRenderer());

        //TODO: AÑADIR BARRA DE BUSQUEDA
        grid.setItems(servicioService.findAll());

        add(grid);
    }

    private static ComponentRenderer<Text, Servicio> createServicioDetailsRenderer() {
        return new ComponentRenderer<>(servicio -> new Text(servicio.getDescription()));
    }

    private void editServicio(Servicio servicio) {
        servicioEditor.editServicio(servicio);
        servicioEditor.setCallback(() -> {
            grid.setItems(servicioService.findAll());
        });

        Dialog dialog = new Dialog();
        H2 headline = new H2("Edit Servicio");
        dialog.add(headline);
        headline.getElement().getClassList().add("draggable");

        dialog.add(servicioEditor);

        dialog.setDraggable(true);
        dialog.setResizable(true); //TODO : CUESTIONARSE EL SI DEBERIA DE SER RESIZABLE

        dialog.open();
        dialog.addDialogCloseActionListener(e-> {
            grid.setItems(servicioService.findAll());
            dialog.close();
        });
    }

    private void addServicio() {
        servicioAdder.setCallback(() -> {
            grid.setItems(servicioService.findAll());
        });
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

    private void deleteServicio(Servicio servicio) {
        Dialog dialog = new Dialog();
        H2 headline = new H2("Delete Servicio");
        dialog.add(headline);
        headline.getElement().getClassList().add("draggable");
        Text message = new Text("¿ Seguro que quieres borrar el servicio ?");
        dialog.add(message);

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        Button deleteButton = new Button("Delete", e -> {
            servicioService.delete(servicio);
            grid.setItems(servicioService.findAll());
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
