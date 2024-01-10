package es.uca.iw.eslada.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RolRepository rolRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RolRepository rolRepository){
        this.passwordEncoder=passwordEncoder;
        this.userRepository=userRepository;
        this.rolRepository = rolRepository;
    }

    public List<Rol> findAllRoles(){
        return rolRepository.findAll();
    }
    public boolean registerUser(User user, Rol selectedRol){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(new HashSet<>(Collections.singletonList(selectedRol)));
            userRepository.save(user);
            return true;
        }catch (DataIntegrityViolationException e){
            return false;
        }
    }

    public List<User> findAll() { return userRepository.findAll(); }


}
