package es.uca.iw.eslada.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    public List<User>  FindByName(String name);
    public List<User> FindByNameOrDniAllIgnoreCase(String name, String dni);

}
