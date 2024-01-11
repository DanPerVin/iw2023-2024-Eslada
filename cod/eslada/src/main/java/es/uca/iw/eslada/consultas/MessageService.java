package es.uca.iw.eslada.consultas;


import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private ConsultationRepository consultationRepository;

    public MessageService(MessageRepository messageRepository, ConsultationRepository consultationRepository) {
        this.messageRepository = messageRepository;
        this.consultationRepository = consultationRepository;
    }

    public List<Message> getMessagesFromConsultation(Consultation consultation) {
        return messageRepository.findByConsultationOrderByCreationDate(consultation);
    }

    public void saveMessage(Message message){
        messageRepository.save(message);
    }

}
