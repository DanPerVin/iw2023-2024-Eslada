package es.uca.iw.eslada.register;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.iw.eslada.main.MainLayout;
import es.uca.iw.eslada.user.Rol;
import es.uca.iw.eslada.user.User;
import es.uca.iw.eslada.user.UserService;
import jakarta.annotation.security.RolesAllowed;
import jdk.jfr.Name;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import java.util.List;
import javax.swing.*;
import java.awt.*;

@Route(value = "register")
@AnonymousAllowed
public class UserRegisterView extends VerticalLayout {
    private final UserService userService;
    private final TextField username = new TextField();
    private final TextField name = new TextField();
    private final TextField surname = new TextField();
    private final TextField dni = new TextField();
    private final EmailField email = new EmailField();
    private final PasswordField password= new PasswordField();
    private final PasswordField password2 = new PasswordField();
    private final Button register = new Button("Registrarse");
    private final BeanValidationBinder<User> binder;
    public UserRegisterView(UserService userService){
        this.userService = userService;

        add(new H1("Regístrate como usuario"));
        add(new H2("Introduce tus datos para empezar a formar parte de Eslada:"));

        username.setId("username");
        name.setId("name");
        surname.setId("surname");
        email.setId("email");
        dni.setId("dni");
        password.setId("password");

        dni.setPattern("^[0-9]{8}[A-Za-z]$");
        dni.setMaxLength(9);
        dni.setMinLength(9);
        dni.setHelperText("Formato: 58723465C");

        password.setMinLength(6);
        password.setMaxLength(26);
        password.setHelperText("6-26 letras y numeros");

        password2.setMinLength(6);
        password2.setMaxLength(26);
        password2.setHelperText("Las contraseñas deben coincidir");

        username.setLabel("Username");
        name.setLabel("Nombre");
        surname.setLabel("Apellidos");
        dni.setLabel("DNI");
        email.setLabel("e-mail");
        password.setLabel("Contraseña");
        password2.setLabel("Repite tu contraseña");



        add(username,name,surname ,dni,email,password,password2,register);

        register.addClickListener(e -> onRegisterButtonClick());

        binder = new BeanValidationBinder<>(User.class);
        binder.bindInstanceFields(this);

        binder.setBean(new User());

        this.setAlignItems(Alignment.CENTER);
    }

    private void onRegisterButtonClick() {
        if(binder.validate().isOk() & password.getValue().equals(password2.getValue())){
            Rol selectedRol = userService.findRolByName("USER");
            if(selectedRol != null && userService.registerUser(binder.getBean(),selectedRol)){
                Notification.show("Registro Realizado !");
                binder.setBean(new User());
                password2.setValue("");
            }else{
                Notification.show("Algo fue mal");
            }
        }else{
            Notification.show("Por favor, revisa los datos introducidos");
        }
    }
}