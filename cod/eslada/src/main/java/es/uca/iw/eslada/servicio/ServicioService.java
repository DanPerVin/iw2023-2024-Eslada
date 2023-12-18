package es.uca.iw.eslada.servicio;

import org.springframework.stereotype.Service;

@Service
public class ServicioService {
    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository){
        this.servicioRepository = servicioRepository;
    }
}
