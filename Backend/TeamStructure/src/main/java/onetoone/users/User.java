package onetoone.users;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


@Entity
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    private String userName;
    private String userType;
    private String email;
    private String password;
    private String phoneNumber;


    @JsonIgnore
    @OneToOne
    private User user;
    public User() {
        // You can initialize any default values here if needed.
    }

    public User(String userName, String userType, String email,String password, String phoneNumber){
        this.userName = userName;
        this.userType = userType;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;

    }


    public int getUserID() {
        return id;
    }

    public void setUserID(int userID) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
