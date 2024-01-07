package es.uca.iw.eslada.consultas;

import es.uca.iw.eslada.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultationService {

    //TODO: Crear las operaciones entre repositorio y servicio

    private MessageRepository messageRepository;
    private ConsultationRepository consultationRepository;

    public ConsultationService(MessageRepository messageRepository, ConsultationRepository consultationRepository) {
        this.messageRepository = messageRepository;
        this.consultationRepository = consultationRepository;
    }

    public List<Consultation> findAll() { return consultationRepository.findAll(); }

    public List<Consultation> findByClosed(Boolean closed) { return consultationRepository.findByClosed(closed); }

    public List<Consultation> findByUser(User user){ return consultationRepository.findByUser(user); };


}
