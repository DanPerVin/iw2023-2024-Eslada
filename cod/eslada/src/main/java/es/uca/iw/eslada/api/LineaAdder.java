package es.uca.iw.eslada.api;

import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import es.uca.iw.eslada.servicio.Servicio;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@SpringComponent
@UIScope
public class LineaAdder extends VerticalLayout implements KeyNotifier {
    private final ApiService apiService;
    private final TextField name;
    private final TextField surname;
    private final TextField phoneNumber;

    private final Button addButton;
    private final Button cancelButton;
    private Runnable callback;
    private final BeanValidationBinder<CustomerLineRequest> binder;
    public LineaAdder(ApiService apiService){
        this.apiService = apiService;

        this.name = new TextField("Nombre");
        this.surname = new TextField("Apellidos");
        this.phoneNumber = new TextField("Numero de tlf.");

        this.addButton = new Button("Añadir", e -> addLinea());
        this.cancelButton = new Button("Cancelar", e -> cancel());

        this.binder = new BeanValidationBinder<>(CustomerLineRequest.class);
        binder.bindInstanceFields(this);

        HorizontalLayout buttonLayout = new HorizontalLayout(addButton, cancelButton);

        add(name,surname,phoneNumber,buttonLayout);
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    private void cancel() {
        binder.setBean(null);
        getParent().ifPresent(parent -> {
            if(parent instanceof Dialog){
                ((Dialog)parent).close();
            }
        });
    }

    private void addLinea() {
        CustomerLineRequest linea = new CustomerLineRequest();
        if(binder.writeBeanIfValid(linea)){
            linea.setCarrier("eslada");
            ResponseEntity<CustomerLine> response = apiService.postInfo(linea);
            if (response.getStatusCode().is2xxSuccessful()) {
                Notification notification = Notification.show("Creado Correctamente");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                getParent().ifPresent(parent -> {
                    if(parent instanceof Dialog){
                        ((Dialog)parent).close();
                    }
                });
            } else {
                Notification notification = Notification.show("Error en la creación: "+ response.getStatusCode());
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
        if (callback != null) {
            callback.run();
        }
    }
}
