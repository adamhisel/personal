package onetoone.Coaches;
import javax.persistence.*;



import com.fasterxml.jackson.annotation.JsonIgnore;

public class Coach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String userName;
    private String userType;
    private String email;
    private String password;
    private String phoneNumber;
    private String team;
}

