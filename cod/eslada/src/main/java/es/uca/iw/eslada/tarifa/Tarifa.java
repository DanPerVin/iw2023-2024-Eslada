package es.uca.iw.eslada.tarifa;

import es.uca.iw.eslada.servicio.Servicio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "tarifa")

public class Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(length = 100)
    @NotEmpty
    private String titulo;

    @Column(length = 300)
    private String descripcion;

    @Column(length = 4)
    private double precio;

    @Column(length = 100)
    private String Url;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tarifa_servicio",
            joinColumns = @JoinColumn(name = "tarifa_id"),
            inverseJoinColumns = @JoinColumn(name = "servicio_id")
    )
    private Collection<Servicio> servicios;

    public double getPrecio() {
        return precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTitulo() { return titulo; }

    public UUID getId() { return id; }
    public String getUrl() { return Url; }
    public Collection<Servicio> getServicios() {
        return servicios;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setUrl(String Url) { this.Url = Url; }

    public void setServicios(Collection<Servicio> servicios) {
        this.servicios = servicios;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tarifa other)) {
            return false;
        }

        if (id != null) {
            return id.equals(other.getId());
        }
        return super.equals(other);
    }
}