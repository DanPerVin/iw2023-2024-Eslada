package es.uca.iw.eslada.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "appuser")
public class User { //TODO: change fields properties (to fit form)

//    public enum UserRole {
//        USER,
//        MARKETING,
//        FINANCE,
//        ASISTANCE
//    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(length = 100)
    private String username;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Rol> roles;

//    @Enumerated(EnumType.STRING)
//    private UserRole userRole = UserRole.USER; //user by default

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Collection<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Rol> roles) {
        this.roles = roles;
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
