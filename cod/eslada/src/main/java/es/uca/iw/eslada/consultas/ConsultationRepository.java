package es.uca.iw.eslada.consultas;

import es.uca.iw.eslada.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {

    List<Consultation> findByClosed(boolean closed);

    List<Consultation> findByUser(User user);



}