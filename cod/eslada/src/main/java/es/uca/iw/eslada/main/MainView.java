package es.uca.iw.eslada.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.Resource;


@AnonymousAllowed
@Route(value = "")
public class MainView extends VerticalLayout {
    private final Button admin = new Button("Iniciar sesión como Administrador");
    private final Button usuario = new Button("Iniciar sesión como Usuario");
    private final Button register = new Button("Registrarse como Usuario");
    public MainView() {

        Dialog logins = new Dialog();

        admin.addClickListener(e -> {
            UI.getCurrent().navigate("admin_view");
            logins.close();
        });
        usuario.addClickListener(e -> {
            UI.getCurrent().navigate("user_view");
            logins.close();
        });
        register.addClickListener(e -> UI.getCurrent().navigate("register"));

        logins.setHeaderTitle("Selecciona como quiere iniciar sesion:");
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(usuario, admin);
        logins.add(dialogLayout);


        addClassNames("mainViewClass");

        setAlignItems(Alignment.CENTER);
        setHorizontalComponentAlignment(Alignment.CENTER);

        Div card = new Div();
        VerticalLayout contents = new VerticalLayout();
        Div container = new Div();
        Image image = new Image("themes/my-theme/logo.png", "Logo de Eslada");
        HorizontalLayout buttons = new HorizontalLayout();
        Div registerCont = new Div();
        Button showLogins = new Button("Iniciar Sesión", e -> logins.open());



        image.addClassNames("logoImage");
        card.addClassNames("card");
        container.addClassNames("container");
        contents.addClassNames("mainViewCardContents");

        contents.setAlignSelf(Alignment.CENTER);
        contents.setJustifyContentMode(JustifyContentMode.CENTER);

        setPadding(true);


        container.add(new H2("Bienvenido a ESLADA"));



        buttons.add(showLogins);
        registerCont.add(register);

        contents.add(container, image, buttons, registerCont);

        card.add(contents);



        add(card);
    }

}
