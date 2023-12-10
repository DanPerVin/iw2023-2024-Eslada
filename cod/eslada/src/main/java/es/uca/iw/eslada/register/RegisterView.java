package es.uca.iw.eslada.register;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.iw.eslada.user.User;
import es.uca.iw.eslada.user.UserService;
import jdk.jfr.Name;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;

import java.awt.*;

@Route("register")
@PageTitle("ESLADA-REGISTER")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {
    private final UserService userService;
    private final TextField name = new TextField();
    private final TextField dni = new TextField();
    private final EmailField email = new EmailField();
    private final PasswordField password= new PasswordField();
    private final PasswordField password2 = new PasswordField();
    private final Button register = new Button("Register");
    private final BeanValidationBinder<User> binder;
    public RegisterView(UserService userService){
        this.userService = userService;

        name.setId("name");
        email.setId("email");
        dni.setId("dni");
        password.setId("password");

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

        register.addClickListener(e -> onRegisterButtonClick());

        binder = new BeanValidationBinder<>(User.class);
        binder.bindInstanceFields(this);

        binder.setBean(new User());
    }

    private void onRegisterButtonClick() {
        if(binder.validate().isOk() & password.getValue().equals(password2.getValue())){
            if(userService.registerUser(binder.getBean())){
                Notification.show("User Created");
                binder.setBean(new User());
                password2.setValue("");
            }else{
                Notification.show("Something went wrong");
            }
        }else{
            Notification.show("Please, revise input data");
        }
    }
}
