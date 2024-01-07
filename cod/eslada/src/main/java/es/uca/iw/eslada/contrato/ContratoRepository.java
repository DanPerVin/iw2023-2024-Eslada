package es.uca.iw.eslada.contrato;

import es.uca.iw.eslada.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContratoRepository extends JpaRepository<Contrato, UUID> {

   List<Contrato> findAllByUserIs(User user);

}
