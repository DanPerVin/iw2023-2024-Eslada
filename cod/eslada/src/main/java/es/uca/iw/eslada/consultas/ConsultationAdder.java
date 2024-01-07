package es.uca.iw.eslada.consultas;

import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import es.uca.iw.eslada.user.AuthenticatedUser;
import es.uca.iw.eslada.user.User;

import java.util.Date;
import java.util.Optional;


@SpringComponent
@UIScope
public class ConsultationAdder extends VerticalLayout implements KeyNotifier {

    private final ConsultationService consultationService;
    private final AuthenticatedUser authenticatedUser;


    private TextField name;
    private Date creationDate;
    private Optional<User> user = Optional.of(new User());
    private boolean closed;
    private Button addButton;
    private Button cancelButton;
    private BeanValidationBinder<Consultation> binder;

    private Runnable callback;

    public ConsultationAdder(ConsultationService consultationService, AuthenticatedUser authenticatedUser) {
        this.consultationService = consultationService;
        this.authenticatedUser = authenticatedUser;

        user = authenticatedUser.get();
        name = new TextField("Asunto");

        addButton = new Button("Crear", e -> add());
        cancelButton = new Button("Cancelar", e -> cancel());
        binder = new BeanValidationBinder<>(Consultation.class);
        binder.bindInstanceFields(this);
        HorizontalLayout buttonLayout = new HorizontalLayout(addButton, cancelButton);

        add(name);
        add(buttonLayout);
    }

    private void cancel() {
        binder.setBean(null);
        getParent().ifPresent(parent -> {
            if (parent instanceof Dialog) {
                ((Dialog) parent).close();
            }
        });
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    private void add() {
        Consultation consultation = new Consultation();
        creationDate = new Date();
        closed = false;

        consultation.setClosed(closed);
        consultation.setCreationDate(creationDate);
        consultation.setName(name.getValue());
        consultation.setUser(user.get());
        
        binder.writeBeanIfValid(consultation);

        if(consultation.getName() != null && user.isPresent() && creationDate != null) {
            consultationService.save(consultation);
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
        UI.getCurrent().navigate("/message/" + consultation.getId().toString());
    }

}
