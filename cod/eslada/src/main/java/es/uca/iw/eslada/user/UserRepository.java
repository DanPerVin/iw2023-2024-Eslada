package es.uca.iw.eslada.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    public List<User>  findByName(String name);
    public List<User> findByNameOrDniAllIgnoreCase(String name, String dni);

    User findByUsername(String username);
}
