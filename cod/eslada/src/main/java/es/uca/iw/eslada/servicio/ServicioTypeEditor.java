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
public class ServicioTypeEditor extends VerticalLayout implements KeyNotifier {
    private final ServicioService servicioService;

    private final TextField name;

    private BeanValidationBinder<ServicioType> binder;
    private Button saveButton;
    private Button cancelButton;

    private ServicioType servicioType;
    private Runnable callback;

    public ServicioTypeEditor(ServicioService servicioService){
        this.servicioService = servicioService;

        this.name = new TextField("Nombre");

        this.saveButton = new Button("Guardar", e->save());
        this.cancelButton = new Button("Cancelar", e -> cancel());

        this.binder = new BeanValidationBinder<>(ServicioType.class);
        binder.bindInstanceFields(this);
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);

        add(name,buttonLayout);

    }
    public void setCallback(Runnable callback) {
        this.callback = callback;
    }
    public void editServicioType(ServicioType servicioType){
        this.servicioType = servicioType;
        binder.setBean(servicioType);
    }
    private void cancel() {
        binder.setBean(null);
        getParent().ifPresent(parent -> {
            if(parent instanceof Dialog){
                ((Dialog)parent).close();
            }
        });
    }

    private void save() {
        if(binder.validate().isOk()){
            servicioService.saveServicioType(servicioType);
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
