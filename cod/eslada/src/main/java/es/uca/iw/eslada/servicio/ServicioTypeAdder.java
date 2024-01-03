package es.uca.iw.eslada.servicio;

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
public class ServicioTypeAdder extends VerticalLayout implements KeyNotifier {
    private final ServicioService servicioService;
    private final TextField name;

    private Button addButton;
    private Button cancelButton;

    private BeanValidationBinder<ServicioType> binder;
    private Runnable callback;
    public ServicioTypeAdder(ServicioService servicioService){
        this.servicioService = servicioService;

        this.name = new TextField("Nombre");

        this.addButton = new Button("Añadir", e -> add());
        this.cancelButton = new Button("Cancelar", e -> cancel());

        this.binder = new BeanValidationBinder<>(ServicioType.class);
        binder.bindInstanceFields(this);

        HorizontalLayout buttonLayout = new HorizontalLayout(addButton,cancelButton);

        add(name,buttonLayout);

    }
    public void setCallback(Runnable callback) {
        this.callback = callback;
    }
    private void add(){
        ServicioType servicioType = new ServicioType();
        binder.writeBeanIfValid(servicioType);

        if (servicioType.getName() != null) {
            servicioService.saveServicioType(servicioType);
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
    private void cancel() {
        binder.setBean(null);
        getParent().ifPresent(parent -> {
            if(parent instanceof Dialog){
                ((Dialog)parent).close();
            }
        });
    }

}
