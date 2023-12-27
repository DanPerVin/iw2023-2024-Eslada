package es.uca.iw.eslada.servicio;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ServicioRepository extends JpaRepository<Servicio,UUID> {
    void deleteById(UUID id);

    List<Servicio> findServiciosByServicioType(ServicioType servicioType);
}
