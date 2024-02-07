package onetoone.users;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
public class UserService {
    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional
    public void createUser(User user) {
        userRepository.save(user);
    }


    public User getUser(int id){
        return userRepository.findById(id);
    }


    public User findByID(int i) {return userRepository.findById(i);}
}
