package es.uca.iw.eslada.factura;

import es.uca.iw.eslada.contrato.Contrato;
import es.uca.iw.eslada.user.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;

    public FacturaService(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    public List<Factura> findAll() { return facturaRepository.findAll(); }

    public List<Factura> findFacturasByUserId(UUID id) { return facturaRepository.findFacturasByUserId(id); }

    public List<Factura> findFacturasByContratoId(UUID id) { return facturaRepository.findFacturasByContratoId(id); }

    public boolean save(Factura factura, Contrato contrato, User user){
        try{
            factura.setUser(user);
            factura.setContrato(contrato);
            factura.setFecha(LocalDateTime.now());
            facturaRepository.save(factura);
            return true;
        }catch (DataIntegrityViolationException e){
            return false;
        }
    }
}
