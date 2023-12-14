package es.uca.iw.eslada;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.iw.eslada.pruebas.PruebaView;


@AnonymousAllowed
@Route(value = "")
public class MainView extends VerticalLayout {
    private final Button admin = new Button("login Admin");
    private final Button usuario = new Button("login User");
    public MainView() {

        admin.addClickListener(e -> UI.getCurrent().navigate(PruebaView.class));
        usuario.addClickListener(e -> UI.getCurrent().navigate(PruebaView.class));

        add(admin, usuario); //TODO: Ponerlo bonito
    }

}
