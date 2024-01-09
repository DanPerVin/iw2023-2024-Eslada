package es.uca.iw.eslada.api;

import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
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

@SpringComponent
@UIScope
public class LineaEditor extends VerticalLayout implements KeyNotifier {

    private final ApiService apiService;

    private final LineaService lineaService;

    private final TextField username;

    private final Checkbox roaming;

    private final Checkbox block;

    private final TextField name;

    private final TextField surname;

    private final TextField phoneNumber;

    private final Button saveButton;
    private final Button cancelButton;

    private Runnable callback;

    private CustomerLine line;

    private Linea linea;

    private BeanValidationBinder<CustomerLineRequest> binder;

    public LineaEditor(ApiService apiService, LineaService lineaService){
        this.apiService = apiService;
        this.lineaService = lineaService;

        this.username = new TextField("User");
        this.username.setReadOnly(true);

        H4 opciones = new H4("Opciones especiales: ");
        HorizontalLayout checboxes = new HorizontalLayout();
        this.roaming = new Checkbox("Roaming");
        this.block = new Checkbox("Bloqueo de Numeros especiales");
        checboxes.add(roaming,block);

        this.name = new TextField("Nombre");
        this.surname = new TextField("Apellidos");
        this.phoneNumber = new TextField("Numero de tlf.");

        this.saveButton = new Button("Guardar",e->save());
        this.cancelButton = new Button("Cancelar", e -> cancel());

        this.binder = new BeanValidationBinder<>(CustomerLineRequest.class);
        binder.bindInstanceFields(this);

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        add(username,opciones,checboxes,name,surname,phoneNumber,buttonLayout);

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

    public void editWrapper(CustomerLineWrapper wrapper){
        editLinea(wrapper.getLinea());
        editLine(wrapper.getCustomerLine());
    }

    public void editLinea(Linea linea){
        this.linea = linea;
        this.username.setValue(linea.getUser().getUsername());
        this.roaming.setValue(linea.getRoaming());
        this.block.setValue(linea.getBlock());
    }
    public void editLine(CustomerLine line){
        this.line = line;
        this.name.setValue(line.getName());
        this.surname.setValue(line.getSurname());
        this.phoneNumber.setValue(line.getPhoneNumber());
    }

    private void save() {
        CustomerLineRequest request = new CustomerLineRequest();
        if(binder.writeBeanIfValid(request)){
            request.setCarrier("eslada");
            ResponseEntity<CustomerLine> response = apiService.patchInfo(line.getId(),request);
            if (response.getStatusCode().is2xxSuccessful()) {
                linea.setRoaming(roaming.getValue());
                linea.setBlock(block.getValue());
                lineaService.saveLinea(linea);
                Notification notification = Notification.show("Actualizado Correctamente");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                getParent().ifPresent(parent -> {
                    if(parent instanceof Dialog){
                        ((Dialog)parent).close();
                    }
                });
            } else {
                Notification notification = Notification.show("Error en la actualizacion: "+ response.getStatusCode());
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
        if (callback != null) {
            callback.run();
        }
    }
}
