package es.uca.iw.eslada.tarifa;

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
public class TarifaEditor extends VerticalLayout implements KeyNotifier {
    private final TarifaRepository tarifaRepository;
    private TextField titulo;
    private TextField descripcion;
    private TextField url;
    private Button saveButton;
    private Button cancelButton;
    private BeanValidationBinder<Tarifa> binder;
    private Tarifa tarifa;
    private Runnable callback;

    public TarifaEditor(TarifaRepository tarifaRepository){
        this.tarifaRepository = tarifaRepository;

        this.titulo = new TextField("nombre");
        this.descripcion = new TextField("descripcion");
        this.url = new TextField("url");
        this.saveButton = new Button("Guardar",e->save());
        this.cancelButton = new Button("Cancelar", e -> cancel());
        this.binder = new BeanValidationBinder<>(Tarifa.class);
        binder.bindInstanceFields(this);
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        add(titulo, descripcion, url, buttonLayout);
    }

    private void cancel() {
        binder.setBean(null);
        getParent().ifPresent(parent -> {
            if(parent instanceof Dialog){
                ((Dialog)parent).close();
            }
        });
    }

    public void editTarifa(Tarifa tarifa){
        this.tarifa = tarifa;
        binder.setBean(tarifa);
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    private void save(){
        if(binder.validate().isOk()){
            tarifaRepository.save(tarifa);
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
