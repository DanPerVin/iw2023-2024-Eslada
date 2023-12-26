package es.uca.iw.eslada.contrato;

import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import es.uca.iw.eslada.user.AuthenticatedUser;
import es.uca.iw.eslada.user.User;

import java.util.Optional;

@SpringComponent
@UIScope
public class ContratoAdder extends VerticalLayout implements KeyNotifier {
    private final ContratoRepository contratoRepository;
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

    private Runnable callback;

    public ContratoAdder(ContratoRepository contratoRepository, AuthenticatedUser authenticatedUser){
        this.contratoRepository = contratoRepository;
        this.authenticatedUser = authenticatedUser;

        Optional<User> user = Optional.of(new User());

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
        this.addButton = new Button("Añadir", e -> add());
        this.cancelButton = new Button("Cancelar", e -> cancel());
        this.binder = new BeanValidationBinder<>(Contrato.class);
        binder.bindInstanceFields(this);
        HorizontalLayout buttonLayout = new HorizontalLayout(addButton, cancelButton);
        add(nombre, apellidos, email, dni, direccion, iban, buttonLayout);
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
        if (contrato.getNombre() != null) {
            contratoRepository.save(contrato);
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
