package es.uca.iw.eslada.contrato;

import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import es.uca.iw.eslada.servicio.Servicio;
import es.uca.iw.eslada.servicio.ServicioService;
import es.uca.iw.eslada.servicio.ServicioType;
import es.uca.iw.eslada.user.User;

import java.util.*;


@SpringComponent
@UIScope
public class ContratoEditor extends VerticalLayout implements KeyNotifier {
    private CheckboxGroup<Servicio> serviciosCheckboxGroup = new CheckboxGroup<>();
    private final ContratoRepository contratoRepository;
    private TextField nombre;
    private TextField apellidos;
    private TextField email;
    private TextField dni;
    private TextField direccion;
    private TextField iban;
    private Button saveButton;
    private Button cancelButton;
    private BeanValidationBinder<Contrato> binder;
    private Contrato contrato;
    private Runnable callback;
    private final ServicioService servicioService;
    private final ContratoService contratoService;

    private Map<ServicioType, CheckboxGroup<Servicio>> checkboxGroups = new HashMap<>();
    private VerticalLayout serviciosSummary = new VerticalLayout();
    private Set<Servicio> selectedServices;
    private Optional<User> user = Optional.of(new User());


    public ContratoEditor(ContratoRepository contratoRepository, ServicioService servicioService, ContratoService contratoService){
        this.contratoRepository = contratoRepository;
        this.servicioService = servicioService;
        this.contratoService = contratoService;

        this.nombre = new TextField("Nombre");
        this.apellidos = new TextField("Apellidos");
        this.dni = new TextField("dni");
        this.email = new TextField("Email");
        this.direccion = new TextField("Direccion");
        this.iban = new TextField("Cuenta Bancaria");
        add(nombre,apellidos, dni, email, direccion, iban);
        this.selectedServices = new HashSet<>();

        this.binder = new BeanValidationBinder<>(Contrato.class);
        binder.bindInstanceFields(this);
        add(serviciosSummary);
    }

    private void updateSummary() {

        serviciosSummary.removeAll();

        double totalPrice = 0;
        for (Map.Entry<ServicioType, CheckboxGroup<Servicio>> entry : checkboxGroups.entrySet()) {
            ServicioType servicioType = entry.getKey();
            CheckboxGroup<Servicio> checkboxGroup = entry.getValue();
            Set<Servicio> selectedServices = checkboxGroup.getSelectedItems();

            if (!selectedServices.isEmpty()) {
                Grid<Servicio> servicioGrid = new Grid<>();

                servicioGrid.addColumn(Servicio::getName).setHeader("Nombre");
                servicioGrid.addColumn(Servicio::getDescription).setHeader("Descripcion");
                servicioGrid.addColumn(Servicio::getPrice).setHeader("Precio");

                servicioGrid.setItems(selectedServices);

                serviciosSummary.add(new H4(servicioType.getName()), servicioGrid);

                totalPrice += selectedServices.stream().mapToDouble(Servicio::getPrice).sum();
            } else {
                serviciosSummary.add(new H4("Sin servicio de tipo " + servicioType.getName()));
            }
        }
        H4 totalPriceText = new H4("Precio total de servicios escogidos: " + totalPrice + " €");
        serviciosSummary.add(totalPriceText);
    }



    private void cancel() {
        binder.setBean(null);
        getParent().ifPresent(parent -> {
            if(parent instanceof Dialog){
                ((Dialog)parent).close();
            }
        });
    }

    public void editContrato(Contrato contrato) {
        this.contrato = contrato;
        binder.setBean(contrato);
        selectedServices = new HashSet<>(contrato.getServicios());
        for (ServicioType servicioType : servicioService.findAllTypes()) {
            CheckboxGroup<Servicio> checkboxGroup = new CheckboxGroup<>();
            checkboxGroup.setLabel(servicioType.getName());
            List<Servicio> serviciosByType = servicioService.findServiciosByServicioType(servicioType);
            checkboxGroup.setItems(serviciosByType);
            checkboxGroups.put(servicioType, checkboxGroup);
            checkboxGroup.deselectAll();
            for (Servicio servicio : serviciosByType) {
                for(Servicio servicioSel : selectedServices) {
                    if (servicioSel.equals(servicio)) {
                        checkboxGroup.select(servicio);
                    }
                }
            }
            binder.bindInstanceFields(this);
            checkboxGroup.addValueChangeListener(event -> updateSummary());
            add(checkboxGroup);
        }
        H3 serviciosSelecionado = new H3("Resumen de Servicios: ");
        serviciosSummary.add(new H4("Sin servicios seleccionados"));
        add(serviciosSelecionado);
        add(serviciosSummary);

        this.saveButton = new Button("Guardar",e->save(contrato));
        this.cancelButton = new Button("Cancelar", e -> cancel());

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        add(buttonLayout);

    }
    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    private void save(Contrato contrato){
        Collection<Servicio> selectedServicios = new ArrayList<>();
        if(binder.validate().isOk()){
            for(Map.Entry<ServicioType, CheckboxGroup<Servicio>> entry : checkboxGroups.entrySet()){
                Collection<Servicio> selectedServiciosinType = new ArrayList<>(entry.getValue().getSelectedItems());
                if(!selectedServiciosinType.isEmpty()){
                    selectedServicios.addAll(selectedServiciosinType);
                }
            }

            if (contrato.getNombre() != null && !selectedServicios.isEmpty()) {
                contratoService.edit(contrato, selectedServicios);
                System.out.println("Servicios lleno");
                binder.setBean(null);
                getParent().ifPresent(parent -> {
                    if (parent instanceof Dialog) {
                        ((Dialog) parent).close();
                    }
                });
            }
            if (callback != null) {
                callback.run();
            }
        }
    }
}
