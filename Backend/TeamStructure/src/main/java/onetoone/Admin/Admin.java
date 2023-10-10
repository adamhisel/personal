package onetoone.Admin;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String userName;
    private String userType;
    private String email;
    private String password;
    private String phoneNumber;
    public Admin(String userName, String userType, String email,String password, String phoneNumber){
        this.userName = userName;
        this.userType = userType;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;

    }
    public int getAdminID() {
        return id;
    }

    public void setAdminID(int userID) {
        this.id = id;
    }

    public String getAdminName() {
        return userName;
    }

    public void setAdminName(String userName) {
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