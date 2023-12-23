package es.uca.iw.eslada.tarifa;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import es.uca.iw.eslada.main.MainLayout;
import jakarta.annotation.security.PermitAll;

import java.util.stream.Stream;

@Route(value = "tarifa_lista", layout = MainLayout.class)
@PermitAll
public class TarifaListView extends VerticalLayout {
    private final TarifaService tarifaService;
    private final Grid<Tarifa> grid = new Grid<>(Tarifa.class, false);

    public TarifaListView(TarifaService tarifaService) {
        this.tarifaService = tarifaService;

        buildUI();
    }

    private void buildUI() {
        add(new H1("Tarifas"));
        grid.addColumn(createTarifaRenderer()).setHeader("Image")
                .setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(Tarifa::getTitulo).setHeader("Título");
        grid.addColumn(Tarifa::getPrecio).setHeader("Precio").setSortable(true);


        grid.addColumn(createToggleDetailsRenderer(grid));

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

}//TODO: Que se vea la imagen y enlace a la ventana de la tarifa
