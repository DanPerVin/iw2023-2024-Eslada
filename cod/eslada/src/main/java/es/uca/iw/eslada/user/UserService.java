package es.uca.iw.eslada.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public boolean registerUser(User user){ //TODO : Hash users' passwords
        try{
            userRepository.save(user);
            return true;
        }catch (DataIntegrityViolationException e){
            return false;
        }
    }

}
