package es.uca.iw.eslada.contrato;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContratoRepository extends JpaRepository<Contrato, UUID> {

   // public List<Contrato> findByTitulo (String titulo);

}
