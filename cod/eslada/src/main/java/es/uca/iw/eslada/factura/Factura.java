package es.uca.iw.eslada.factura;

import es.uca.iw.eslada.contrato.Contrato;
import es.uca.iw.eslada.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getFecha() { return fecha; }

    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Contrato getContrato() { return contrato; }

    public void setContrato(Contrato contrato) { this.contrato = contrato; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }
}
