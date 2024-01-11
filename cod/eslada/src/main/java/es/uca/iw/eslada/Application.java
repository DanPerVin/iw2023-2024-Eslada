package es.uca.iw.eslada;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import es.uca.iw.eslada.factura.FacturaGenerationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@EnableScheduling
@PWA(name = "Project Base for Vaadin with Spring", shortName = "Project Base")
@Theme(value = "my-theme", variant = Lumo.DARK)
public class Application implements AppShellConfigurator {

    private FacturaGenerationService facturaGenerationService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void ejecutarGeneracionFacturaMensual() {
        facturaGenerationService.generarFacturaMensual();
    }

}
