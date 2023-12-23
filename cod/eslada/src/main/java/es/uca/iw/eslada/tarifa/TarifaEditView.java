package es.uca.iw.eslada.tarifa;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import es.uca.iw.eslada.main.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import java.util.stream.Stream;

@Route(value = "tarifa_edit", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
public class TarifaEditView extends VerticalLayout {
    private final TarifaService tarifaService;
    private final TarifaEditor tarifaEditor;
    private final TarifaAdder tarifaAdder;
    private final Grid<Tarifa> grid = new Grid<>(Tarifa.class, false);

    public TarifaEditView(TarifaService tarifaService, TarifaEditor tarifaEditor, TarifaAdder tarifaAdder) {
        this.tarifaService = tarifaService;
        this.tarifaAdder = tarifaAdder;
        this.tarifaEditor = tarifaEditor;

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        H1 title = new H1("Tarifas");
        Button addButton = new Button("Add", e -> addTarifa());

        headerLayout.add(title, addButton);

        add(headerLayout);

        buildUI();
    }

    private void buildUI() {
        grid.addColumn(createTarifaRenderer()).setHeader("Image")
                .setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(Tarifa::getTitulo).setHeader("Título");
        grid.addColumn(Tarifa::getPrecio).setHeader("Precio").setSortable(true);


        grid.addColumn(createToggleDetailsRenderer(grid));
        grid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, tarifa) -> {
            Button editButton = new Button("Edit", e -> this.editTarifa(tarifa));
            editButton.setIcon(new Icon(VaadinIcon.EDIT));
            layout.add(editButton);

            Button deleteButton = new Button("Delete", e -> this.deleteTarifa(tarifa));
            deleteButton.setIcon(new Icon(VaadinIcon.TRASH));
            layout.add(deleteButton);
        })).setHeader("Acciones");

        grid.setDetailsVisibleOnClick(false);
        grid.setItemDetailsRenderer(createTarifaDetailsRenderer());

        grid.setItems(tarifaService.findAll());

        add(grid);
    }

    private static Renderer<Tarifa> createToggleDetailsRenderer(
            Grid<Tarifa> grid) {
        return LitRenderer.<Tarifa> of(
                        "<vaadin-button theme=\"tertiary\" @click=\"${handleClick}\">Detalles</vaadin-button>")
                .withFunction("handleClick",
                        tarifa -> grid.setDetailsVisible(tarifa,
                                !grid.isDetailsVisible(tarifa)));
    }


    private static Renderer<Tarifa> createTarifaRenderer() {
        return LitRenderer.<Tarifa>of(
                        "<vaadin-avatar img=\"${item.Url}\" name=\"${item.fullName}\" alt=\"User avatar\"></vaadin-avatar>")
                .withProperty("Url", Tarifa::getUrl);

    }

    private static ComponentRenderer<TarifaDetailsFormLayout, Tarifa> createTarifaDetailsRenderer() {
        return new ComponentRenderer<>(TarifaDetailsFormLayout::new,
                TarifaDetailsFormLayout::setTarifa);
    }

    private static class TarifaDetailsFormLayout extends FormLayout {
        private final TextField descripcionField = new TextField("Descripción");

        public TarifaDetailsFormLayout() {
            Stream.of(descripcionField).forEach(field -> {
                field.setReadOnly(true);
                add(field);
            });

            setResponsiveSteps(new ResponsiveStep("0", 3));
            setColspan(descripcionField, 3);
        }

        public void setTarifa(Tarifa tarifa) {
            descripcionField.setValue(tarifa.getDescripcion());
        }
    }

    private void editTarifa(Tarifa tarifa) {
        tarifaEditor.editTarifa(tarifa);
        tarifaEditor.setCallback(() -> {
            grid.setItems(tarifaService.findAll());
        });

        Dialog dialog = new Dialog();
        H2 headline = new H2("Edit Tarifa");
        dialog.add(headline);
        headline.getElement().getClassList().add("draggable");

        dialog.add(tarifaEditor);

        dialog.setDraggable(true);
        dialog.setResizable(true);

        dialog.open();
        dialog.addDialogCloseActionListener(e-> {
            grid.setItems(tarifaService.findAll());
            dialog.close();
        });
    }

    private void addTarifa() {
        tarifaAdder.setCallback(() -> {
            grid.setItems(tarifaService.findAll());
        });
        Dialog dialog = new Dialog();
        H2 headline = new H2("Add Tarifa");
        dialog.add(headline);
        headline.getElement().getClassList().add("draggable");

        dialog.add(tarifaAdder);

        dialog.setDraggable(true);
        dialog.setResizable(true);

        dialog.open();
        dialog.addDialogCloseActionListener(e-> {
            grid.setItems(tarifaService.findAll());
            dialog.close();
        });
    }

    private void deleteTarifa(Tarifa tarifa) {
        Dialog dialog = new Dialog();
        H2 headline = new H2("Delete Tarifa");
        dialog.add(headline);
        headline.getElement().getClassList().add("draggable");
        Text message = new Text("¿ Seguro que quieres borrar la tarifa ?");
        dialog.add(message);

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        Button deleteButton = new Button("Delete", e -> {
            tarifaService.delete(tarifa);
            grid.setItems(tarifaService.findAll());
            dialog.close();
        });

        deleteButton.setThemeName("error");
        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, deleteButton);
        dialog.add(buttonLayout);

        dialog.setDraggable(true);
        dialog.setResizable(true);

        dialog.open();
    }

}//TODO: Que se vea la imagen y enlace a la ventana de la tarifa

