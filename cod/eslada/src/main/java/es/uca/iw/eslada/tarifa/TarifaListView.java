package es.uca.iw.eslada.tarifa;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import es.uca.iw.eslada.main.MainLayout;
import jakarta.annotation.security.PermitAll;

@Route(value = "tarifas", layout = MainLayout.class)
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
        grid.addColumn(createTarifatRenderer()).setHeader("Image")
                .setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(Tarifa::getTitulo).setHeader("Título");
        grid.addColumn(Tarifa::getPrecio).setHeader("Precio");

        grid.setItems(tarifaService.findAll());

        add(grid);
    }

    private static Renderer<Tarifa> createTarifatRenderer() {

        return LitRenderer.<Tarifa>of(
                        "<vaadin-avatar img=\"${item.tarifaUrl}\" name=\"${item.fullName}\" alt=\"User avatar\"></vaadin-avatar>")
                .withProperty("tarifaUrl", Tarifa::getTarifaUrl);

    }
}//TODO: Que se vea la imagen y enlace a la ventana de la tarifa
