package es.uca.iw.eslada.consultas;

import es.uca.iw.eslada.user.User;
import jakarta.persistence.*;

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

    @OneToOne
    private User user;


}
