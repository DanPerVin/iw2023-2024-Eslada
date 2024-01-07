package es.uca.iw.eslada.consultas;


import org.springframework.stereotype.Service;

@Service
public class MessageService {

    //TODO: Operaciones de mensajes

    private MessageRepository messageRepository;
    private ConsultationRepository consultationRepository;

    public MessageService(MessageRepository messageRepository, ConsultationRepository consultationRepository) {
        this.messageRepository = messageRepository;
        this.consultationRepository = consultationRepository;
    }

}
