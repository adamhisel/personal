package onetoone.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

        @Transactional
    public void updateUser(int id, String userName, String userType, String email, String password, String phoneNumber) {
        userRepository.updateUserById(id, userName, userType, email, password, phoneNumber);
    }



    @Transactional
    public void createUser(User user) {
        userRepository.save(user);
    }

//    public void getUser(int id){
//        userRepository.getUserbyID(3);
//    }

}
