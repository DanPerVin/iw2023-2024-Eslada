package es.uca.iw.eslada.factura;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FacturaRepository extends JpaRepository<Factura, UUID> {

    List<Factura> findFacturasByUserId(UUID id);
    List<Factura> findFacturasByContratoId(UUID id);

}
