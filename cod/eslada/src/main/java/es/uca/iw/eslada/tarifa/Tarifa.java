package es.uca.iw.eslada.tarifa;

import es.uca.iw.eslada.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

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
    private String tarifaUrl;

    public double getPrecio() {
        return precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public UUID getId() {
        return id;
    }
    public String getTarifaUrl(){return tarifaUrl; }

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

    public void setTarifaUrl(String tarifaUrl) { this.tarifaUrl = tarifaUrl; }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User other)) {
            return false;
        }

        if (id != null) {
            return id.equals(other.getId());
        }
        return super.equals(other);
    }
}