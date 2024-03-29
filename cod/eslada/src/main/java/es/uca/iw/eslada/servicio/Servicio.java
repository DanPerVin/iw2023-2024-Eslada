package es.uca.iw.eslada.servicio;

import es.uca.iw.eslada.contrato.Contrato;
import es.uca.iw.eslada.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.Collection;
import java.util.UUID;

@Entity
public class Servicio{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(length = 100)
    @NotEmpty
    private String name;

    @Column(length = 1000)
    @Lob
    private String description;

    @Column
    private double price;

    @ManyToOne
    @JoinColumn(name="servicioType_id", nullable = false)
    private ServicioType servicioType;

    @ManyToMany(mappedBy = "servicios")
    private Collection<Contrato> contratos;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ServicioType getServicioType() {
        return servicioType;
    }

    public void setServicioType(ServicioType servicioType) {
        this.servicioType = servicioType;
    }

    public Collection<Contrato> getContratos() {
        return contratos;
    }

    public void setContratos(Collection<Contrato> contratos) {
        this.contratos = contratos;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Servicio other)) {
            return false;
        }

        if (id != null) {
            return id.equals(other.id);
        }
        return super.equals(other);
    }

    @Override
    public String toString() {
        return name;
    }
}
