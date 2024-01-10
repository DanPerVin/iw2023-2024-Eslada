package es.uca.iw.eslada.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RolRepository extends JpaRepository<Rol, UUID> {

    Rol findFirstByName(String name);

}
