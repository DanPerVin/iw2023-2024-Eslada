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

@SpringComponent
@UIScope
public class ContratoEditor extends VerticalLayout implements KeyNotifier {
    private final ContratoRepository contratoRepository;
    private TextField nombre;
    private TextField apellidos;
    private TextField email;
    private TextField dni;
    private TextField direccion;
    private TextField iban;
    private Button saveButton;
    private Button cancelButton;
    private BeanValidationBinder<Contrato> binder;
    private Contrato contrato;
    private Runnable callback;

    public ContratoEditor(ContratoRepository contratoRepository){
        this.contratoRepository = contratoRepository;

        this.nombre = new TextField("Nombre");
        this.apellidos = new TextField("Apellidos");
        this.dni = new TextField("dni");
        this.email = new TextField("Email");
        this.direccion = new TextField("Direccion");
        this.iban = new TextField("Cuenta Bancaria");
        this.saveButton = new Button("Guardar",e->save());
        this.cancelButton = new Button("Cancelar", e -> cancel());
        this.binder = new BeanValidationBinder<>(Contrato.class);
        binder.bindInstanceFields(this);
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        add(nombre,apellidos, dni, email, direccion, iban, buttonLayout);
    }

    private void cancel() {
        binder.setBean(null);
        getParent().ifPresent(parent -> {
            if(parent instanceof Dialog){
                ((Dialog)parent).close();
            }
        });
    }

    public void editContrato(Contrato contrato){
        this.contrato = contrato;
        binder.setBean(contrato);
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    private void save(){
        if(binder.validate().isOk()){
            contratoRepository.save(contrato);
            binder.setBean(null);
            getParent().ifPresent(parent -> {
                if(parent instanceof Dialog){
                    ((Dialog)parent).close();
                }
            });
            if (callback != null) {
                callback.run();
            }
        }
    }
}
