package onetoone.users;
import onetoone.Admin.Admin;
import onetoone.Admin.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import onetoone.Players.Player;
import onetoone.Players.PlayerRepository;



import java.util.List;

@RestController
public class UserController {
    private final UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    AdminRepository adminRepository;

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
    CreateUserRequest createUser(@RequestBody CreateUserRequest request) {
        if (request == null) {
            throw new RuntimeException();
        }

        switch (request.getUserType()) {
            case "player":
                Player player = new Player(
                        request.getUserName(),
                        request.getUserType(),
                        request.getEmail(),
                        request.getPassword(),
                        request.getPhoneNumber(),
                        request.getPlayerName(),
                        request.getNumber(),
                        request.getPosition()
                );
                playerRepository.save(player);
                break;

            case "admin":
                Admin admin = new Admin(
                        request.getUserName(),
                        request.getUserType(),
                        request.getEmail(),
                        request.getPassword(),
                        request.getPhoneNumber()
                );
                adminRepository.save(admin);
                break;

            default:
                // Handle the case where userType is not recognized
                break;
        }
        return request;
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

    @GetMapping("loginUser/{userName}/{password}")
    User loginUser(@PathVariable String userName, @PathVariable String password){
        return userRepository.findByuserNameAndPassword(userName,password);
    }

    }

