package es.uca.iw.eslada.main;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "user_view", layout = MainLayout.class)
@RolesAllowed("ROLE_USER")
public class UserHomeView extends VerticalLayout {

    public UserHomeView(){
        add(new H1("Bienvenido User"));
    }

}
