package es.uca.iw.eslada.servicio;

import es.uca.iw.eslada.servicio.ServicioType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServicioTypeRepository extends JpaRepository<ServicioType, UUID> {

}
