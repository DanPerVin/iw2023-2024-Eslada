package es.uca.iw.eslada.servicio;

import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.combobox.ComboBox;
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

import java.util.List;

@SpringComponent
@UIScope
public class ServicioEditor extends VerticalLayout implements KeyNotifier {
    private final ServicioService servicioService;

    private TextField name;
    private TextArea description;
    private NumberField price;
    private Button saveButton;
    private Button cancelButton;

    private final ComboBox<ServicioType> typeComboBox = new ComboBox<>("Tipo");
    private BeanValidationBinder<Servicio> binder;
    private Servicio servicio;
    private Runnable callback;

    public ServicioEditor(ServicioService servicioService){
        this.servicioService =servicioService;

        this.name = new TextField("Nombre");
        this.description = new TextArea("Descripcion");
        this.price = new NumberField("Precio");

        typeComboBox.setItemLabelGenerator(ServicioType::getName);
        typeComboBox.setClearButtonVisible(true);
        typeComboBox.setPlaceholder("Selecciona un tipo");
        List<ServicioType> availableTypes = servicioService.findAllTypes();
        typeComboBox.setItems(availableTypes);

        this.saveButton = new Button("Guardar",e->save());
        this.cancelButton = new Button("Cancelar", e -> cancel());
        this.binder = new BeanValidationBinder<>(Servicio.class);
        binder.bindInstanceFields(this);
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        add(name,description,price,typeComboBox, buttonLayout);
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
        ServicioType type = typeComboBox.getValue();
        if(binder.validate().isOk() && type!=null){
            servicioService.saveServicio(servicio,type);
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
