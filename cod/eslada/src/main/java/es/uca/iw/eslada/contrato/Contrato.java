package es.uca.iw.eslada.contrato;

import es.uca.iw.eslada.servicio.Servicio;
import es.uca.iw.eslada.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "contrato")
public class Contrato {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(length = 30)
    @NotEmpty
    private String nombre;

    @Column(length = 70)
    private String apellidos;

    @Column(length = 100)
    @Email
    private String email;

    @Column(length = 9)
    private String dni;

    @Column(length = 100)
    private String direccion;

    @Column(length = 24)
    private String iban;

    @Column
    private LocalDateTime fecha;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "contrato_servicio",
            joinColumns = @JoinColumn(name = "contrato_id"),
            inverseJoinColumns = @JoinColumn(name = "servicio_id")
    )
    private Collection<Servicio> servicios;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    public UUID getId() { return id; }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getEmail() { return email; }

    public String getDni() { return dni; }

    public String getDireccion() { return direccion; }

    public String getIban() { return iban; }

    public LocalDateTime getFecha() { return fecha; }

    public Collection<Servicio> getServicios() {
        return servicios;
    }

    public User getUser() { return user; }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setEmail(String email) { this.email = email; }

    public void setDni(String dni) { this.dni = dni; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public void setIban(String iban) { this.iban = iban; }

    public void setFecha(LocalDateTime fecha) {this.fecha = fecha; }

    public void setServicios(Collection<Servicio> servicios) {
        this.servicios = servicios;
    }

    public void setUser(User user) { this.user = user;}

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Contrato other)) {
            return false;
        }

        if (id != null) {
            return id.equals(other.getId());
        }
        return super.equals(other);
    }
}