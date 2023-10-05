package onetoone.users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;
    @Autowired
    UserRepository userRepository;



    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable int id){
        return userRepository.findById(id);
    }

    @PostMapping(path = "/users")
    String createUser(@RequestBody User user){
        if (user == null)
            return failure;
        userRepository.save(user);
        return success;
    }
    @PostMapping("/updateUser/{id}")
    public void updateUser(@PathVariable int id, @RequestBody UserUpdateRequest request) {
        userService.updateUser(id,
                request.getUserName(),
                request.getUserType(),
                request.getEmail(),
                request.getPassword(),
                request.getPhoneNumber());
    }

    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
        return success;
    }

//    @GetMapping("loginUser/{userName}/{password}")
//    User loginUser(@PathVariable String userName, String password){
//        return userRepository.findLogin(userName,password);
//    }

    }

