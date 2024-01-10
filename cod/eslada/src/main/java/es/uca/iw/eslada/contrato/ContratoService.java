package es.uca.iw.eslada.contrato;

import es.uca.iw.eslada.servicio.Servicio;
import es.uca.iw.eslada.user.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;


@Service
public class ContratoService {

    private final ContratoRepository contratoRepository;

    public ContratoService(ContratoRepository contratoRepository) {
        this.contratoRepository = contratoRepository;
    }

    public List<Contrato> findAll() {
        return contratoRepository.findAll();
    }

    public List<Contrato> findContratosByUserId(UUID id) {return contratoRepository.findContratosByUserId(id); }


    public void delete(Contrato contrato) {
        contratoRepository.deleteById(contrato.getId());
    }

    public boolean save(Contrato contrato, User user, Collection<Servicio> servicios){
        try{
            contrato.setUser(user);
            contrato.setServicios(servicios);
            contratoRepository.save(contrato);
            return true;
        }catch (DataIntegrityViolationException e){
            return false;
        }
    }

    public boolean edit(Contrato contrato, Collection<Servicio> servicios){
        try{
            contrato.setServicios(servicios);
            contratoRepository.save(contrato);
            return true;
        }catch (DataIntegrityViolationException e){
            return false;
        }
    }

    public String getServiciosNames(Contrato contrato) {
        return contrato.getServicios().stream()
                .map(Servicio::getName)
                .collect(Collectors.joining(", "));
    }

    public double getServiciosPrecio(Contrato contrato){
        double precioTotal = 0;
        Collection<Servicio> servicios = contrato.getServicios();
        for (Servicio servicio : servicios) {
            precioTotal += servicio.getPrice();
        }
        return precioTotal;
    }

    public List<Contrato> findAllByUser(User user){
        return contratoRepository.findAllByUserIs(user);
    }

}
