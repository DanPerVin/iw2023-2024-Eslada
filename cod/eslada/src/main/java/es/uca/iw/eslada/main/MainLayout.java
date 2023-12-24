package es.uca.iw.eslada.main;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import es.uca.iw.eslada.servicio.ServicioView;
import es.uca.iw.eslada.contrato.ContratoEditView;
import es.uca.iw.eslada.contrato.ContratoListView;


public class MainLayout extends AppLayout {

    private H2 viewTitle;
    private AccessAnnotationChecker accessChecker;
    private final transient AuthenticationContext authContext;

    public MainLayout(AccessAnnotationChecker accessChecker, AuthenticationContext authContext) {
        this.accessChecker = accessChecker;
        this.authContext = authContext;

        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        Button logoutButton = new Button("Logout", new Icon(VaadinIcon.SIGN_OUT_ALT), click -> authContext.logout());
        logoutButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        logoutButton.getElement().getStyle().set("margin-left", "auto");
        logoutButton.getElement().getStyle().set("margin-right", "10px");

        addToNavbar(true, toggle, viewTitle, logoutButton);
    }

    private void addDrawerContent() {

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(scroller);
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        if (accessChecker.hasAccess(UserHomeView.class)) {
            nav.addItem(new SideNavItem("Home", UserHomeView.class, VaadinIcon.HOME.create()));
        }

        if (accessChecker.hasAccess(EmployeeHomeView.class)) {
            nav.addItem(new SideNavItem("Home", EmployeeHomeView.class, VaadinIcon.HOME.create()));
        }

        if (accessChecker.hasAccess(ContratoListView.class)) {
            nav.addItem(new SideNavItem("Contrato List", ContratoListView.class, VaadinIcon.CASH.create()));
        }

        if (accessChecker.hasAccess(ContratoEditView.class)) {
            nav.addItem(new SideNavItem("Contrato Edit List", ContratoEditView.class, VaadinIcon.CASH.create()));
        }

        if(accessChecker.hasAccess(ServicioView.class)){
            nav.addItem(new SideNavItem("Servicios List", ServicioView.class,VaadinIcon.ANCHOR.create()));
        }

        return nav;
    }//TODO: Revisar permisos de tarifas
}
