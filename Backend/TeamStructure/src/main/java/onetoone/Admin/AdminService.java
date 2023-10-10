package onetoone.Admin;

import onetoone.users.User;
import onetoone.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Transactional
    public void updateUser(int id, String userName, String userType, String email, String password, String phoneNumber) {
        adminRepository.updateAdminById(id, userName, userType, email, password, phoneNumber);
    }



    @Transactional
    public void createUser(Admin admin) {
        adminRepository.save(admin);
    }

//    public void getUser(int id){
//        userRepository.getUserbyID(3);
//    }

}
