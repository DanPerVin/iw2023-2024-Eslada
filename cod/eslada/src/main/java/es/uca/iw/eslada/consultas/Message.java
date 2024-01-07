package es.uca.iw.eslada.consultas;

import es.uca.iw.eslada.user.User;
import jakarta.persistence.*;
import org.hibernate.mapping.ToOne;

import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private Consultation consultation;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(length = 600)
    private String messageString;

    @ManyToOne
    private User user;

    public UUID getId() {
        return id;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getMessageString() {
        return messageString;
    }

    public void setMessageString(String messageString) {
        this.messageString = messageString;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
