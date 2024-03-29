package es.uca.iw.eslada.login;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import es.uca.iw.eslada.pruebas.PruebaView;
import es.uca.iw.eslada.user.AuthenticatedUser;
@AnonymousAllowed
@PageTitle("Login pruebitas")
@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver {
    private final AuthenticatedUser authenticatedUser;
    private final LoginForm login = new LoginForm();

    public LoginView(AuthenticatedUser authenticatedUser){
        this.authenticatedUser = authenticatedUser;
        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Eslada");
        i18n.getHeader().setDescription("Login usando User o admin");
        setI18n(i18n);
        //i18n.setAdditionalInformation(null);
        setForgotPasswordButtonVisible(false);
        setOpened(true);
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(authenticatedUser.get().isPresent()){
            setOpened(false);
            event.forwardTo(PruebaView.class);
        }
        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }
}
