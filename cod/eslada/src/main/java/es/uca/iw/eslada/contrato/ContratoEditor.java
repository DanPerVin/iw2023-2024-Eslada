package es.uca.iw.eslada.contrato;

import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataProvider;
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

    private Map<ServicioType, MultiSelectComboBox<Servicio>> comboboxes = new HashMap<>();

    private VerticalLayout serviciosSummary = new VerticalLayout();
    private Set<Servicio> selectedServices= new HashSet<>();;



    public ContratoEditor(ServicioService servicioService, ContratoService contratoService){
        this.servicioService = servicioService;
        this.contratoService = contratoService;
        
        this.nombre = new TextField("Nombre");
        this.apellidos = new TextField("Apellidos");
        this.dni = new TextField("dni");
        this.email = new TextField("Email");
        this.direccion = new TextField("Direccion");
        this.iban = new TextField("Cuenta Bancaria");
        add(nombre,apellidos, dni, email, direccion, iban);


        this.binder = new BeanValidationBinder<>(Contrato.class);
        binder.bindInstanceFields(this);

        for (ServicioType servicioType : servicioService.findAllTypes()) {
            MultiSelectComboBox<Servicio> comboBox = new MultiSelectComboBox<>();
            comboBox.setLabel(servicioType.getName());
            List<Servicio> serviciosByType = servicioService.findServiciosByServicioType(servicioType);
            comboBox.setItems(serviciosByType);
            comboboxes.put(servicioType, comboBox);
            comboBox.addValueChangeListener(event -> updateSummary());
            add(comboBox);
        }

        H3 header = new H3("Resumen de Servicios: ");
        add(header);
        serviciosSummary.add(new H4("Sin servicios seleccionados"));
        add(serviciosSummary);

        this.saveButton = new Button("Guardar",e->save(contrato));
        this.cancelButton = new Button("Cancelar", e -> cancel());

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        add(buttonLayout);

    }

    private void updateSummary() {

        serviciosSummary.removeAll();

        double totalPrice = 0;
        for (Map.Entry<ServicioType, MultiSelectComboBox<Servicio>> entry : comboboxes.entrySet()) {
            ServicioType servicioType = entry.getKey();
            MultiSelectComboBox<Servicio> comboBox = entry.getValue();
            Set<Servicio> selectedServices = comboBox.getSelectedItems();

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

    public void clear(){
        serviciosSummary.removeAll();
    }

    private void cancel() {
        binder.setBean(null);
        clear();
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

        for(Map.Entry<ServicioType, MultiSelectComboBox<Servicio>> entry : comboboxes.entrySet()){
            ServicioType servicioType = entry.getKey();
            MultiSelectComboBox<Servicio> comboBox = entry.getValue();
            comboBox.deselectAll();
            for(Servicio servicio: selectedServices){
                if(servicio.getServicioType().equals(servicioType)){
                    comboBox.select(servicio);

                }
            }


        }

        updateSummary();


    }
    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    private void save(Contrato contrato){
        Collection<Servicio> selectedServicios = new ArrayList<>();
        if(binder.validate().isOk()){
            for(Map.Entry<ServicioType, MultiSelectComboBox<Servicio>> entry : comboboxes.entrySet()){
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
