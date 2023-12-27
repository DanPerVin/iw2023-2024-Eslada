package es.uca.iw.eslada.servicio;

import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.data.binder.BeanValidationBinder;

@SpringComponent
@UIScope
public class ServicioEditor extends VerticalLayout implements KeyNotifier {
    private final ServicioRepository servicioRepository;

    private TextField name;
    private TextArea description;
    private NumberField price;
    private Button saveButton;
    private Button cancelButton;
    private BeanValidationBinder<Servicio> binder;
    private Servicio servicio;
    private Runnable callback;

    public ServicioEditor(ServicioRepository servicioRepository){
        this.servicioRepository =servicioRepository;

        this.name = new TextField("Nombre");
        this.description = new TextArea("Descripcion");
        this.price = new NumberField("Precio");
        this.saveButton = new Button("Guardar",e->save());
        this.cancelButton = new Button("Cancelar", e -> cancel());
        this.binder = new BeanValidationBinder<>(Servicio.class);
        binder.bindInstanceFields(this);
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        add(name,description,price, buttonLayout);
    }

    private void cancel() {
        binder.setBean(null);
        getParent().ifPresent(parent -> {
            if(parent instanceof Dialog){
                ((Dialog)parent).close();
            }
        });
    }

    public void editServicio(Servicio servicio){
        this.servicio = servicio;
        binder.setBean(servicio);
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    private void save(){
        if(binder.validate().isOk()){
            servicioRepository.save(servicio);
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
