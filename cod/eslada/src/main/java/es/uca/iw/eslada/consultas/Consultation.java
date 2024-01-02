package es.uca.iw.eslada.consultas;


import es.uca.iw.eslada.user.User;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "consultation")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(length = 100)
    private String name;

    @OneToOne
    private User user;

}