package es.uca.iw.eslada.contrato;

import es.uca.iw.eslada.servicio.Servicio;
import es.uca.iw.eslada.user.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ContratoService {

    private final ContratoRepository contratoRepository;

    public ContratoService(ContratoRepository contratoRepository) {
        this.contratoRepository = contratoRepository;
    }

    public List<Contrato> findAll() {
        return contratoRepository.findAll();
    }


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
}
