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

@Route(value = "serviciostype", layout = MainLayout.class)
@RolesAllowed({"ROLE_ADMIN","ROLE_MARKETING"})
public class ServicioTypeView extends VerticalLayout {
    private final ServicioService servicioService;
    private final ServicioTypeAdder servicioTypeAdder;
    private final ServicioTypeEditor servicioTypeEditor;
    private final Grid<ServicioType> grid = new Grid<>(ServicioType.class,false);
    public ServicioTypeView(ServicioService servicioService,ServicioTypeAdder servicioTypeAdder, ServicioTypeEditor servicioTypeEditor){
        this.servicioService = servicioService;
        this.servicioTypeAdder = servicioTypeAdder;
        this.servicioTypeEditor = servicioTypeEditor;

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        H1 title = new H1("Tipos de Servicios");
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
        Dialog dialog = new Dialog();
        H2 headline = new H2("Delete ServicioType");
        dialog.add(headline);
        headline.getElement().getClassList().add("draggable");
        Text message = new Text("¿ Seguro que quieres borrar el servicio-type? tenga en cuenta que se borrarán todos los servicios con este tipo");
        dialog.add(message);

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        Button deleteButton = new Button("Delete", e -> {
            servicioService.deleteServicioType(servicioType);
            grid.setItems(servicioService.findAllTypes());
            dialog.close();
        });

        deleteButton.setThemeName("error");
        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, deleteButton);
        dialog.add(buttonLayout);

        dialog.setDraggable(true);
        dialog.setResizable(true);

        dialog.open();
    }

    private void editServicioType(ServicioType servicioType) {
        servicioTypeEditor.editServicioType(servicioType);
        servicioTypeEditor.setCallback(() -> {
            grid.setItems(servicioService.findAllTypes());
        });

        Dialog dialog = new Dialog();
        H2 headline = new H2("Edit Servicio Type");
        dialog.add(headline);
        headline.getElement().getClassList().add("draggable");

        dialog.add(servicioTypeEditor);

        dialog.setDraggable(true);
        dialog.setResizable(true); //TODO : CUESTIONARSE EL SI DEBERIA DE SER RESIZABLE

        dialog.open();
        dialog.addDialogCloseActionListener(e-> {
            grid.setItems(servicioService.findAllTypes());
            dialog.close();
        });
    }

    private void addServicioType() {
        servicioTypeAdder.setCallback(()->{
            grid.setItems(servicioService.findAllTypes());
        });
        Dialog dialog = new Dialog();
        H2 headline = new H2("Add ServicioType");
        dialog.add(headline);
        headline.getElement().getClassList().add("draggable");

        dialog.add(servicioTypeAdder);
        dialog.setDraggable(true);
        dialog.setResizable(true);

        dialog.open();
        dialog.addDialogCloseActionListener(e-> {
            grid.setItems(servicioService.findAllTypes());
            dialog.close();
        });
    }
}
