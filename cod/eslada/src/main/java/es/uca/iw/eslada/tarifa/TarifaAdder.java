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
public class TarifaAdder extends VerticalLayout implements KeyNotifier {
    private final TarifaRepository tarifaRepository;

    private TextField titulo;
    private Button addButton;
    private Button cancelButton;
    private BeanValidationBinder<Tarifa> binder;

    private Runnable callback;

    public TarifaAdder(TarifaRepository tarifaRepository){
        this.tarifaRepository = tarifaRepository;

        this.titulo = new TextField("nombre");
        this.addButton = new Button("Add", e -> add());
        this.cancelButton = new Button("Cancelar", e -> cancel());
        this.binder = new BeanValidationBinder<>(Tarifa.class);
        binder.bindInstanceFields(this);
        HorizontalLayout buttonLayout = new HorizontalLayout(addButton, cancelButton);
        add(titulo, buttonLayout);
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
        Tarifa tarifa = new Tarifa();
        binder.writeBeanIfValid(tarifa);
        if (tarifa.getTitulo() != null) {
            tarifaRepository.save(tarifa);
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
