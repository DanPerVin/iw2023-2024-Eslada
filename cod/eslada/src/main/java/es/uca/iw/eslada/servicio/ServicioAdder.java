package es.uca.iw.eslada.servicio;

import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.data.binder.BeanValidationBinder;


@SpringComponent
@UIScope
public class ServicioAdder extends VerticalLayout implements KeyNotifier {
    private final ServicioRepository servicioRepository;

    private TextField name;
    private Button addButton;
    private Button cancelButton;
    private BeanValidationBinder<Servicio> binder;

    private Runnable callback;

    public ServicioAdder(ServicioRepository servicioRepository){
        this.servicioRepository = servicioRepository;

        this.name = new TextField("nombre");
        this.addButton = new Button("Add", e -> add());
        this.cancelButton = new Button("Cancelar", e -> cancel());
        this.binder = new BeanValidationBinder<>(Servicio.class);
        binder.bindInstanceFields(this);
        HorizontalLayout buttonLayout = new HorizontalLayout(addButton, cancelButton);
        add(name, buttonLayout);
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
        Servicio servicio = new Servicio();
        binder.writeBeanIfValid(servicio);
        if (servicio.getName() != null) {
            servicioRepository.save(servicio);
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