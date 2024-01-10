package es.uca.iw.eslada.api;

import es.uca.iw.eslada.user.User;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class Linea {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private String line;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean roaming; //Activación de Roaming

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean block; //Bloqueo de numeros especiales

    //TODO:COMPARTIMIENTO DE LINEAS.

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getRoaming() {
        return roaming;
    }

    public void setRoaming(Boolean roaming) {
        this.roaming = roaming;
    }

    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }

//    public Set<Linea> getSharedDataLines() {
//        return sharedDataLines;
//    }
//
//    public void setSharedDataLines(Set<Linea> sharedDataLines) {
//        this.sharedDataLines = sharedDataLines;
//    }


}
