package es.uca.iw.eslada.servicio;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class ServicioType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(length = 100)
    private String name;

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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ServicioType other)) {
            return false;
        }

        if (id != null) {
            return id.equals(other.id);
        }
        return super.equals(other);
    }
}
