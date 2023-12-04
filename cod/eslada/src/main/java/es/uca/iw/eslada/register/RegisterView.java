package es.uca.iw.eslada.register;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.eslada.user.UserService;
import jdk.jfr.Name;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;

import java.awt.*;

@Route("register")
@PageTitle("ESLADA-REGISTER")
public class RegisterView extends VerticalLayout {
    public RegisterView(@Autowired UserService userService){
        TextField name = new TextField();
        TextField dni = new TextField();
        EmailField email = new EmailField();
        PasswordField password= new PasswordField();
        PasswordField password2 = new PasswordField();
        Button register = new Button("Register");

        dni.setPattern("^[0-9]{8}[A-Za-z]$");
        dni.setMaxLength(9);
        dni.setMinLength(9);
        dni.setHelperText("Format: 58723465C");

        password.setMinLength(6);
        password.setMaxLength(26);
        password.setHelperText("6-26 characters and numbers");

        password2.setMinLength(6);
        password2.setMaxLength(26);
        password2.setHelperText("Passwords must coincide");

        name.setLabel("Name");
        dni.setLabel("DNI");
        email.setLabel("e-mail");
        password.setLabel("Password");
        password2.setLabel("Repeat Password");

        add(name,dni,email,password,password2,register);

        //register.addClickListener(e -> onRegisterButtonClick());
    }

    private void onRegisterButtonClick() {
    }
}
