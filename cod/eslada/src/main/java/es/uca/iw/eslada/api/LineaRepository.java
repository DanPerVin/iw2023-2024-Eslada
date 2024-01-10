package es.uca.iw.eslada.api;

import es.uca.iw.eslada.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LineaRepository extends JpaRepository<Linea, UUID> {
    void deleteAllByLine(String line);
    Linea findByLineIs(String line);
    List<Linea> findAllByUser(User user);
}
