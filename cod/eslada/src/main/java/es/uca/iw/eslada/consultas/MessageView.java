package es.uca.iw.eslada.consultas;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import es.uca.iw.eslada.main.MainLayout;
import jakarta.annotation.security.RolesAllowed;


@Route(value = "message", layout = MainLayout.class)
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
public class MessageView extends VerticalLayout {



    public MessageView() {

        HorizontalLayout C1 = new HorizontalLayout();
        C1.add(new H1("Consulta: ")); //TODO: ADD CONSULTATION_ID

        VerticalLayout C2 = new VerticalLayout();
        //TODO: ADD CHAT


        HorizontalLayout C3 = new HorizontalLayout();
        //TODO: ADD TEXTFIELD & BTN




        add(C1);
    }
}
