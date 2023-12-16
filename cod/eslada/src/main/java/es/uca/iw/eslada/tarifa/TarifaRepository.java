package es.uca.iw.eslada.tarifa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TarifaRepository extends JpaRepository<Tarifa, UUID> {

   // public List<Tarifa> findByTitulo (String titulo);

}
