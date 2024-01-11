package es.uca.iw.eslada.api;

import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.http.ResponseEntity;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringComponent
@UIScope
public class LineaDetailer extends VerticalLayout implements KeyNotifier {
    private final ApiService apiService;
    private final DatePicker startDate;
    private final DatePicker endDate;
    private final Button searchButton;
    private final Button cancelButton;
    private final Grid<DataUsageRecord> dataUsageRecordGrid = new Grid<>(DataUsageRecord.class);
    private final Grid<CallRecord> callRecordGrid = new Grid<>(CallRecord.class);
    private  CustomerLine line;

    private Runnable callback;

    public LineaDetailer(ApiService apiService){
        this.apiService = apiService;


        HorizontalLayout dateLayout = new HorizontalLayout();
        this.startDate = new DatePicker("Fecha de Inicio");
        this.endDate = new DatePicker("Fecha de Fin");
        dateLayout.add(startDate,endDate);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        this.searchButton = new Button("Buscar", e-> search());
        this.cancelButton = new Button("Cancelar", e->cancel());
        buttonLayout.add(searchButton,cancelButton);

        add(dateLayout,buttonLayout);

        add(new H4("Uso de datos: "));
        add(dataUsageRecordGrid);
        add(new H4("Historial de llamadas: "));
        add(callRecordGrid);
    }
    public void setCallback(Runnable callback) {
        this.callback = callback;
    }
    private void cancel() {
        clear();

        getParent().ifPresent(parent -> {
            if(parent instanceof Dialog){
                ((Dialog)parent).close();
            }
        });
    }

    private void search() {
        if(startDate.getValue()!=null && endDate.getValue()!=null) {
            LocalDate startDateValue = startDate.getValue();
            LocalDate endDateValue = endDate.getValue();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedStartDate = startDateValue.format(formatter);
            String formattedEndDate = endDateValue.format(formatter);

            ResponseEntity<List<DataUsageRecord>> responsedata = apiService.getDataUsageRecord(line.getId(), formattedStartDate, formattedEndDate);
            List<DataUsageRecord> dataUsageRecords = responsedata.getBody();
            dataUsageRecordGrid.setItems(dataUsageRecords);

            ResponseEntity<List<CallRecord>> responsecall = apiService.getCallRecord(line.getId(),formattedStartDate,formattedEndDate);
            List<CallRecord> callRecords = responsecall.getBody();
            callRecordGrid.setItems(callRecords);
        }else{
            ResponseEntity<List<DataUsageRecord>> responsedata = apiService.getDataUsageRecord(line.getId(), null, null);
            List<DataUsageRecord> dataUsageRecords = responsedata.getBody();
            dataUsageRecordGrid.setItems(dataUsageRecords);

            ResponseEntity<List<CallRecord>> responsecall = apiService.getCallRecord(line.getId(),null,null);
            List<CallRecord> callRecords = responsecall.getBody();
            callRecordGrid.setItems(callRecords);
        }


    }

    public void setLine(CustomerLine line) {
        this.line = line;
    }

    public void clear(){
        this.startDate.setValue(null);
        this.endDate.setValue(null);
        dataUsageRecordGrid.setItems();
        callRecordGrid.setItems();
    }

}
