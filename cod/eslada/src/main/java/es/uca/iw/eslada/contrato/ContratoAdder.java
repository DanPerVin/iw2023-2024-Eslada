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
public class ContratoAdder extends VerticalLayout implements KeyNotifier {
    private final ContratoRepository contratoRepository;

    private TextField nombre;
    private Button addButton;
    private Button cancelButton;
    private BeanValidationBinder<Contrato> binder;

    private Runnable callback;

    public ContratoAdder(ContratoRepository contratoRepository){
        this.contratoRepository = contratoRepository;

        this.nombre = new TextField("Nombre");
        this.addButton = new Button("Add", e -> add());
        this.cancelButton = new Button("Cancelar", e -> cancel());
        this.binder = new BeanValidationBinder<>(Contrato.class);
        binder.bindInstanceFields(this);
        HorizontalLayout buttonLayout = new HorizontalLayout(addButton, cancelButton);
        add(nombre, buttonLayout);
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
