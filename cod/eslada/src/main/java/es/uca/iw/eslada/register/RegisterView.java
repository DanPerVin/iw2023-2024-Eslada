package es.uca.iw.eslada.register;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.eslada.user.UserService;
import jdk.jfr.Name;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.textfield.TextField;

import java.awt.*;

@Route("register")
@PageTitle("ESLADA-REGISTER")
public class RegisterView extends VerticalLayout {
    public RegisterView(@Autowired UserService userService){
        TextField textField = new TextField("Bienvenido al registro");

        add(textField);
    }
}
