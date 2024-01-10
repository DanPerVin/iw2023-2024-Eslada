package es.uca.iw.eslada.factura;

import com.vaadin.flow.data.binder.BeanValidationBinder;
import es.uca.iw.eslada.contrato.Contrato;
import es.uca.iw.eslada.contrato.ContratoService;
import es.uca.iw.eslada.user.User;
import es.uca.iw.eslada.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacturaGenerationService {

    public final FacturaService facturaService;
    public final ContratoService contratoService;
    public final UserService userService;
    private Runnable callback;
    private BeanValidationBinder<Factura> binder;



    public FacturaGenerationService(FacturaService facturaService, ContratoService contratoService, UserService userService){
        this.facturaService = facturaService;
        this.contratoService = contratoService;
        this.userService = userService;
        this.binder = new BeanValidationBinder<>(Factura.class);
//        binder.bindInstanceFields(this);
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    public void generarFacturaMensual() {
        List<User> usuarios = userService.findAll();

        for(User user: usuarios){
            List<Contrato> contratos = contratoService.findContratosByUserId(user.getId());
            for(Contrato contrato: contratos){
                Factura factura = new Factura();
                binder.writeBeanIfValid(factura);

                try{
                    facturaService.save(factura, contrato, user);
                    binder.setBean(null);
                    if (callback != null) {
                        callback.run();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
