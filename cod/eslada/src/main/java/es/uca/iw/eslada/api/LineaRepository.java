package es.uca.iw.eslada.api;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LineaRepository extends JpaRepository<Linea, UUID> {
    void deleteByLineIs(String line);
}
