package es.uca.iw.eslada.main;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "admin_view")
@RolesAllowed("ROLE_ADMIN")
public class EmployeeHomeView extends VerticalLayout {

    public EmployeeHomeView(){
        add(new H1("Bienvenido Admin"));
    }

}
