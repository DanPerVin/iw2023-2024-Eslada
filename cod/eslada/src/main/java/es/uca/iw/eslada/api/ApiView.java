package es.uca.iw.eslada.api;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.iw.eslada.main.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

import java.util.Arrays;
import java.util.List;

@Route(value = "api", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
public class ApiView extends VerticalLayout {
    //TODO: BORRAR PAGINA DE PRUEBAS DE API

    private final ApiService apiService;
    private final LineaAdder lineaAdder;

    private final LineaEditor lineaEditor;

    private final LineaDetailer lineaDetailer;

    private final TextField searchBar;

    private final Button searchButton;

    private final Grid<CustomerLine> grid = new Grid<>(CustomerLine.class);
    public ApiView(ApiService apiService, LineaAdder lineaAdder, LineaEditor lineaEditor, LineaDetailer lineaDetailer){
        this.apiService =  apiService;
        this.lineaAdder = lineaAdder;
        this.lineaEditor = lineaEditor;
        this.lineaDetailer = lineaDetailer;


        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        H1 title = new H1("Lineas");;
        Button addButton = new Button("Añadir linea", e -> addLinea());
        headerLayout.add(title, addButton);
        add(headerLayout);

        HorizontalLayout searchFunction = new HorizontalLayout();
        this.searchBar = new TextField();
        this.searchBar.setPlaceholder("Buscar Número de tlf");
        this.searchBar.setClearButtonVisible(true);
        this.searchButton = new Button(new Icon(VaadinIcon.SEARCH), e -> searchNumber());
        searchFunction.add(searchBar,searchButton);

        add(searchFunction);
        add(new H4("EL resultado es : "));

        grid.setColumns("carrier","id","name","surname", "phoneNumber");

        grid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, line) -> {
            Button editButton = new Button("Editar", e -> this.editLine(line));
            editButton.setIcon(new Icon(VaadinIcon.EDIT));
            layout.add(editButton);

            Button deleteButton = new Button("Borrar", e -> this.deleteLine(line));
            deleteButton.setIcon(new Icon(VaadinIcon.TRASH));
            layout.add(deleteButton);

            Button detailsButton = new Button("Detalles", e -> this.detailsLine(line));
            detailsButton.setIcon(new Icon(VaadinIcon.PLUS));
            layout.add(detailsButton);
        })).setHeader("Acciones");

        fetchData();


        add(grid);
    }

    private void searchNumber() {
        String phoneNumber = searchBar.getValue();
        if (!phoneNumber.isEmpty()) {
            ResponseEntity<CustomerLine> response = apiService.searchByPhoneNumber(phoneNumber);
            if (response.getBody() != null) {
                grid.setItems(Arrays.asList(response.getBody()));
            } else {
                Notification.show("No se encuentra linea con numero de tlf: " + phoneNumber, 3000, Notification.Position.MIDDLE);
            }
        } else {
            fetchData();
        }
        searchBar.clear();
    }



    private void detailsLine(CustomerLine line) {
        lineaDetailer.setLine(line);
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

    private void deleteLine(CustomerLine line) {
        Dialog dialog = new Dialog();
        H2 headline = new H2("Delete Line");
        dialog.add(headline);
        headline.getElement().getClassList().add("draggable");
        Text message = new Text("¿ Seguro que quieres borrar la linea ?");
        dialog.add(message);

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        Button deleteButton = new Button("Delete", e -> {
            apiService.deleteInfo(line.getId());
            fetchData();
            dialog.close();
        });

        deleteButton.setThemeName("error");
        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, deleteButton);
        dialog.add(buttonLayout);

        dialog.setDraggable(true);
        dialog.setResizable(true);

        dialog.open();
    }

    private void addLinea() {

        lineaAdder.setCallback(() -> {
            fetchData();
        });

        Dialog dialog = new Dialog();
        H2 headline = new H2("Añadir Linea");
        dialog.add(headline);
        headline.getElement().getClassList().add("draggable");

        dialog.add(lineaAdder);

        dialog.setDraggable(true);
        dialog.setResizable(true);

        dialog.open();
        dialog.addDialogCloseActionListener(e-> {
            fetchData();
            dialog.close();
        });
    }

    private void fetchData() {
        ResponseEntity<List<CustomerLine>> response = apiService.getInfo();
        List<CustomerLine> customerLines = response.getBody();

        grid.setItems(customerLines);
    }

    private void editLine(CustomerLine line) {
        lineaEditor.editLine(line);
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
