package es.uca.iw.eslada.consultas;

import es.uca.iw.eslada.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public Optional<Consultation> findById(UUID id) { return consultationRepository.findById(id); }

    public void closeConsultation(Consultation consultation) {
        consultation.setClosed(true);
        consultationRepository.save(consultation);
    }

    public void save(Consultation consultation) {
        consultationRepository.save(consultation);
    }


}
