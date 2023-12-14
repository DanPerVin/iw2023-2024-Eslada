package es.uca.iw.eslada.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;


@AnonymousAllowed
@Route(value = "")
public class MainView extends VerticalLayout {
    private final Button admin = new Button("login Admin");
    private final Button usuario = new Button("login User");
    public MainView() {

        admin.addClickListener(e -> UI.getCurrent().navigate("admin_view"));
        usuario.addClickListener(e -> UI.getCurrent().navigate("user_view"));

        add(admin, usuario); //TODO: Ponerlo bonito
    }

}
