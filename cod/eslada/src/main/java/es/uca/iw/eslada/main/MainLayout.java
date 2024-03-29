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
import es.uca.iw.eslada.api.ApiView;
import es.uca.iw.eslada.api.UserApiView;
import es.uca.iw.eslada.consultas.ConsultationView;
import es.uca.iw.eslada.register.RegisterView;
import es.uca.iw.eslada.servicio.ServicioView;
import es.uca.iw.eslada.contrato.ContratoEditView;
import es.uca.iw.eslada.contrato.ContratoListView;
import es.uca.iw.eslada.factura.FacturaAdminView;
import es.uca.iw.eslada.factura.FacturaUserView;
import es.uca.iw.eslada.servicio.ServicioTypeView;
import es.uca.iw.eslada.servicio.ServicioView;


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
            nav.addItem(new SideNavItem("Casa", UserHomeView.class, VaadinIcon.HOME.create()));
        }

        if (accessChecker.hasAccess(EmployeeHomeView.class)) {
            nav.addItem(new SideNavItem("Casa", EmployeeHomeView.class, VaadinIcon.HOME.create()));
        }

        if(accessChecker.hasAccess(ServicioView.class)){
            nav.addItem(new SideNavItem("Lista de Servicios", ServicioView.class,VaadinIcon.SUITCASE.create()));
        }
        if(accessChecker.hasAccess(ServicioTypeView.class)){
            nav.addItem(new SideNavItem("Tipos de Servicio", ServicioTypeView.class,VaadinIcon.CHART.create()));
        }

        if (accessChecker.hasAccess(ContratoListView.class)) {
            nav.addItem(new SideNavItem("Mis Contratos", ContratoListView.class, VaadinIcon.CASH.create()));
        }

        if (accessChecker.hasAccess(ContratoEditView.class)) {
            nav.addItem(new SideNavItem("Lista de Contratos", ContratoEditView.class, VaadinIcon.CASH.create()));
        }
        if (accessChecker.hasAccess(ApiView.class)) {
            nav.addItem(new SideNavItem("Lineas telefonicas", ApiView.class, VaadinIcon.PHONE.create()));
        }
        if (accessChecker.hasAccess(UserApiView.class)) {
            nav.addItem(new SideNavItem("Lineas telefonicas", UserApiView.class, VaadinIcon.PHONE.create()));
        }


        if(accessChecker.hasAccess(ConsultationView.class)){
            nav.addItem(new SideNavItem("Consultas", ConsultationView.class,VaadinIcon.CHAT.create()));
        }

        if(accessChecker.hasAccess(RegisterView.class)){
            nav.addItem(new SideNavItem("Registrar Usuario", RegisterView.class,VaadinIcon.USER.create()));
        }

        if (accessChecker.hasAccess(FacturaUserView.class)) {
            nav.addItem(new SideNavItem("Mis Facturas", FacturaUserView.class, VaadinIcon.BOOK_DOLLAR.create()));
        }

        if (accessChecker.hasAccess(FacturaAdminView.class)) {
            nav.addItem(new SideNavItem("Lista de Facturas", FacturaAdminView.class, VaadinIcon.BOOK_DOLLAR.create()));
        }

        return nav;
    }
}
