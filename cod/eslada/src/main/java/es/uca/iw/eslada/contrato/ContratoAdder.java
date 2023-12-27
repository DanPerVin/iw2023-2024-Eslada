package es.uca.iw.eslada.contrato;

import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import es.uca.iw.eslada.servicio.Servicio;
import es.uca.iw.eslada.servicio.ServicioService;
import es.uca.iw.eslada.servicio.ServicioType;
import es.uca.iw.eslada.user.AuthenticatedUser;
import es.uca.iw.eslada.user.User;

import java.util.*;

@SpringComponent
@UIScope
public class ContratoAdder extends VerticalLayout implements KeyNotifier {
    private final ContratoService contratoService;

    private final ServicioService servicioService;
    private final AuthenticatedUser authenticatedUser;

    private TextField nombre;
    private TextField apellidos;
    private TextField email;
    private TextField dni;
    private TextField direccion;
    private TextField iban;
    private Button addButton;
    private Button cancelButton;
    private BeanValidationBinder<Contrato> binder;

    private Optional<User> user = Optional.of(new User());

    private Map<ServicioType, MultiSelectComboBox<Servicio>> comboboxes = new HashMap<>();

    private Runnable callback;

    public ContratoAdder(ContratoService contratoService,ServicioService servicioService, AuthenticatedUser authenticatedUser){
        this.contratoService = contratoService;
        this.servicioService = servicioService;
        this.authenticatedUser = authenticatedUser;

//        Optional<User> user = Optional.of(new User());

        user = authenticatedUser.get();
        this.nombre = new TextField("Nombre");
        nombre.setValue(user.get().getName());
        nombre.setReadOnly(true);
        this.apellidos = new TextField("Apellidos");
        apellidos.setValue(user.get().getSurname());
        apellidos.setReadOnly(true);
        this.email = new TextField("Email");
        email.setValue(user.get().getEmail());
        email.setReadOnly(true);
        this.dni = new TextField("D.N.I");
        dni.setValue(user.get().getDni());
        dni.setReadOnly(true);
        this.direccion = new TextField("Dirección");
        this.iban = new TextField("Cuenta corriente");

        HorizontalLayout nombre_apellidos = new HorizontalLayout(nombre,apellidos);
        HorizontalLayout email_dni = new HorizontalLayout(email,dni);
        HorizontalLayout direccion_iban = new HorizontalLayout(direccion,iban);
        

        this.addButton = new Button("Añadir", e -> add());
        this.cancelButton = new Button("Cancelar", e -> cancel());
        this.binder = new BeanValidationBinder<>(Contrato.class);
        binder.bindInstanceFields(this);
        HorizontalLayout buttonLayout = new HorizontalLayout(addButton, cancelButton);


        add(nombre_apellidos,email_dni, direccion_iban);
        for(ServicioType servicioType : servicioService.findAllTypes()){
            MultiSelectComboBox<Servicio> comboBox = new MultiSelectComboBox<>();
            comboBox.setLabel(servicioType.getName());
            List<Servicio> serviciosByType = servicioService.findServiciosByServicioType(servicioType);
            comboBox.setItems(serviciosByType);
            comboboxes.put(servicioType, comboBox);
            add(comboBox);
        }

        add(buttonLayout);

    }

    private void cancel() {
        binder.setBean(null);
        getParent().ifPresent(parent -> {
            if(parent instanceof Dialog){
                ((Dialog)parent).close();
            }
        });
    }
    public void setCallback(Runnable callback) {
        this.callback = callback;
    }
    private void add(){
        Contrato contrato = new Contrato();
        binder.writeBeanIfValid(contrato);
        Collection<Servicio> selectedServicios = new ArrayList<>();

        for(Map.Entry<ServicioType, MultiSelectComboBox<Servicio>> entry : comboboxes.entrySet()){
            Collection<Servicio> selectedServiciosinType = new ArrayList<>(entry.getValue().getSelectedItems());
            if(!selectedServiciosinType.isEmpty()){
                selectedServicios.addAll(selectedServiciosinType);
            }
        }

        if (contrato.getNombre() != null && !selectedServicios.isEmpty() && user.isPresent()) {
            contratoService.save(contrato,user.get(),selectedServicios);
            binder.setBean(null);
            getParent().ifPresent(parent -> {
                if(parent instanceof Dialog){
                    ((Dialog)parent).close();
                }
            });
        }
        if (callback != null) {
            callback.run();
        }

    }

}
