package es.uca.iw.eslada.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
public class User {

    public enum UserRole {
        USER,
        MARKETING,
        FINANCE,
        ASSITANCE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(length = 100)
    private String name;

    @Column(length = 9)
    @Size(min = 9,max = 9)
    @Pattern(regexp = "^[0-9]{8}[A-Za-z]$")
    private String dni;

    @Column(length = 100)
    @Email
    private String email;

    @NotEmpty
    @Column(length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public String getDni() {
        return dni;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public UUID getId() {
        return id;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User other)) {
            return false;
        }

        if (id != null) {
            return id.equals(other.id);
        }
        return super.equals(other);
    }

}
