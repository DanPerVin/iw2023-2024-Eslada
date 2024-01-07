package es.uca.iw.eslada.api;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Route("api")
@AnonymousAllowed
public class ApiView extends VerticalLayout {
    //TODO: BORRAR PAGINA DE PRUEBAS DE API

    private final ApiService apiService;

    private final Grid<CustomerLine> grid = new Grid<>(CustomerLine.class);
    public ApiView(ApiService apiService){
        this.apiService =  apiService;

        add(new H1("Página de pruebas"));
        Button postButton = new Button("Send POST Request", event -> postInfo());
        add(postButton);

        add(new H4("EL resultado es : "));
        fetchData();

        add(grid);
    }

    private void postInfo() {
        // Create a new CustomerLineRequest object
        CustomerLineRequest request = new CustomerLineRequest();
        request.setName("a");
        request.setSurname("ab");
        request.setCarrier("ab");
        request.setPhoneNumber("ab");

        // Call the postInfo() method of ApiService
        ResponseEntity<CustomerLine> response = apiService.postInfo(request);

        // Check if the POST request was successful
        if (response.getStatusCode().is2xxSuccessful()) {
            // Fetch the data again to update the grid
            fetchData();
        } else {
            // Log the error message
            System.out.println("Failed to send the POST request: " + response.getStatusCode());
        }
    }

    private void fetchData() {
        ResponseEntity<List<CustomerLine>> response = apiService.getInfo();
        List<CustomerLine> customerLines = response.getBody();
        grid.setItems(customerLines);
    }
}
