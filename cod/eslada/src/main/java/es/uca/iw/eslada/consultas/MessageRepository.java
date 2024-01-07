package es.uca.iw.eslada.consultas;

import org.hibernate.annotations.OrderBy;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {


    List<Message> findByConsultationOrderByCreationDate(Consultation consultation);

}
