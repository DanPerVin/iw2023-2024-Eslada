package es.uca.iw.eslada.consultas;

import es.uca.iw.eslada.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {

    List<Consultation> findByClosed(boolean closed);

    List<Consultation> findByUser(User user);

}