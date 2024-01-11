package es.uca.iw.eslada.api;

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
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.iw.eslada.main.MainLayout;
import es.uca.iw.eslada.user.AuthenticatedUser;
import es.uca.iw.eslada.user.User;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Route(value = "userapi", layout = MainLayout.class)
@RolesAllowed("ROLE_USER")
public class UserApiView extends VerticalLayout {
    private final ApiService apiService;
    private final AuthenticatedUser authenticatedUser;
    private Optional<User> optionalUser;
    private User user;
    private final LineaService lineaService;
    private final LineaDetailer lineaDetailer;
    private final LineaEditor lineaEditor;

    //NAME, SURNAME, CARRIER

    private final Grid<CustomerLineWrapper> grid = new Grid<>(CustomerLineWrapper.class);

    public UserApiView(AuthenticatedUser authenticatedUser, ApiService apiService, LineaService lineaService, LineaDetailer lineaDetailer, LineaEditor lineaEditor){
        this.authenticatedUser = authenticatedUser;
        this.apiService = apiService;
        this.lineaService = lineaService;
        this.lineaDetailer = lineaDetailer;
        this.lineaEditor = lineaEditor;

        optionalUser = authenticatedUser.get();
        if (!optionalUser.isPresent()) {
            add(new H1("No se encuentra Loggeado."));
            return;
        }

        user = optionalUser.get();

        grid.removeAllColumns();

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        headerLayout.add(new H1("Tus lineas"));
        add(headerLayout);

        grid.addColumn(wrapper -> wrapper.getCustomerLine().getCarrier()).setHeader("Carrier").setResizable(true);
        grid.addColumn(wrapper -> wrapper.getCustomerLine().getName()).setHeader("Nombre").setResizable(true);
        grid.addColumn(wrapper -> wrapper.getCustomerLine().getSurname()).setHeader("Apellidos").setResizable(true);
        grid.addColumn(wrapper -> wrapper.getCustomerLine().getPhoneNumber()).setHeader("Numero de tlf.").setResizable(true);

        grid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, wrapper) -> {
            Button editButton = new Button("Editar", e -> this.editLine(wrapper));
            editButton.setIcon(new Icon(VaadinIcon.EDIT));
            layout.add(editButton);

            Button detailsButton = new Button("Datos Y llamadas", e -> this.detailsLine(wrapper));
            detailsButton.setIcon(new Icon(VaadinIcon.PLUS));
            layout.add(detailsButton);
        })).setHeader("Acciones").setResizable(true);

        fetchData();

        add(grid);

        //TODO: CUESTIONAR NECESIDAD DE BARRA DE BUSQUEDA

    }

    private void fetchData(){
        List<Linea> lineas = lineaService.findAllByUser(user);
        List<CustomerLineWrapper> wrappers = new ArrayList<>();

        for(Linea linea : lineas){
            CustomerLineWrapper wrapper = new CustomerLineWrapper();
            ResponseEntity<CustomerLine> customerLineResponseEntity= apiService.getLine(linea.getLine());
            if(customerLineResponseEntity.getBody() != null){
                wrapper.setLinea(linea);
                wrapper.setCustomerLine(customerLineResponseEntity.getBody());
                wrappers.add(wrapper);
            }
        }

        grid.setItems(wrappers);
    }

    private void detailsLine(CustomerLineWrapper wrapper) {
        lineaDetailer.setLine(wrapper.getCustomerLine());
        lineaDetailer.setCallback(() -> {
            fetchData();
        });

        Dialog dialog = new Dialog();
        H2 headline = new H2("Detalles de Linea");
        dialog.add(headline);
        headline.getElement().getClassList().add("draggable");

        dialog.add(lineaDetailer);

        dialog.setDraggable(true);
        dialog.setResizable(true);

        dialog.open();
        dialog.addDialogCloseActionListener(e-> {
            lineaDetailer.clear();
            dialog.close();
        });
    }

    private void editLine(CustomerLineWrapper wrapper) {
        lineaEditor.editWrapper(wrapper);
        lineaEditor.setCallback(() -> {
            fetchData();
        });

        Dialog dialog = new Dialog();
        H2 headline = new H2("Editar Linea");
        dialog.add(headline);
        headline.getElement().getClassList().add("draggable");

        dialog.add(lineaEditor);

        dialog.setDraggable(true);
        dialog.setResizable(true);

        dialog.open();
        dialog.addDialogCloseActionListener(e-> {
            fetchData();
            dialog.close();
        });
    }

}
