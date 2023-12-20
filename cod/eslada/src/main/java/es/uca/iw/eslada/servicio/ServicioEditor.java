package es.uca.iw.eslada.servicio;

import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
    private Button saveButton;
    private Button cancelButton;
    private BeanValidationBinder<Servicio> binder;
    private Servicio servicio;

    public ServicioEditor(ServicioRepository servicioRepository){
        this.servicioRepository =servicioRepository;

        this.name = new TextField("nombre");
        this.saveButton = new Button("Guardar",e->save());
        this.cancelButton = new Button("Cancelar", e -> cancel());
        this.binder = new BeanValidationBinder<>(Servicio.class);
        binder.bindInstanceFields(this);
        add(name,saveButton);
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

    private void save(){
        servicioRepository.save(servicio);
        binder.setBean(null);
    }
}
