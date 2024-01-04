package es.uca.iw.eslada.servicio;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class ServicioService {
    private final ServicioRepository servicioRepository;
    private final  ServicioTypeRepository servicioTypeRepository;

    public ServicioService(ServicioRepository servicioRepository, ServicioTypeRepository servicioTypeRepository){
        this.servicioRepository = servicioRepository;
        this.servicioTypeRepository = servicioTypeRepository;
    }

    public List<Servicio> findAll() {
        return servicioRepository.findAll();
    }

    public List<ServicioType> findAllTypes(){ return servicioTypeRepository.findAll();}

    public void delete(Servicio servicio) {
        servicioRepository.deleteById(servicio.getId());
    }

    public boolean saveServicio(Servicio servicio,ServicioType servicioType) {
        try {
            servicio.setServicioType(servicioType);
            servicioRepository.save(servicio);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    public List<Servicio> findServiciosByServicioType(ServicioType servicioType) {
        return servicioRepository.findServiciosByServicioType(servicioType);
    }

    public void saveServicioType(ServicioType servicioType) { servicioTypeRepository.save(servicioType);}
    public void deleteAllServiciosByServicioType(ServicioType servicioType){
        servicioRepository.deleteAllByServicioType(servicioType);
    }
    @Transactional
    public void deleteServicioType(ServicioType servicioType){
        deleteAllServiciosByServicioType(servicioType);
        servicioTypeRepository.delete(servicioType);
    }
}
