package es.uca.iw.eslada.servicio;

import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ServicioService {
    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository){
        this.servicioRepository = servicioRepository;
    }

    public List<Servicio> findAll() {
        return servicioRepository.findAll();
    }
}
