package es.uca.iw.eslada.pruebas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import es.uca.iw.eslada.user.AuthenticatedUser;
import es.uca.iw.eslada.user.User;
import jakarta.annotation.security.RolesAllowed;

import java.util.Optional;

@RolesAllowed("ROLE_USER")
@PageTitle("Prueba")
@Route("prueba")
public class PruebaView extends VerticalLayout { //TODO: SOLO ACCESO UNA VEZ LOGEADO
    private final transient AuthenticationContext authContext;
    private final AuthenticatedUser authenticatedUser;
    public PruebaView(AuthenticatedUser authenticatedUser, AuthenticationContext authContext){
        this.authenticatedUser = authenticatedUser;
        this.authContext = authContext;
        String username;
        Optional<User> user = Optional.of(new User());
        user = authenticatedUser.get();
        if(user != null){
            username = user.get().getUsername();
            add(username);
        }
        H1 texto = new H1("deberias de estar logeado si has llegado hasta aqui");
        add(texto);
        Button logoutButton = new Button("Logout", click -> authContext.logout());
        add(logoutButton);
    }
}
