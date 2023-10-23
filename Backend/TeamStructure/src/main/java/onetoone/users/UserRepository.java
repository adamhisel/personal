package onetoone.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findById(int id);
    void deleteById(int id);
    @Modifying
<<<<<<< HEAD
    @Query("UPDATE User u SET u.userName = :userName, u.email = :email, u.password = :password, u.phoneNumber = :phoneNumber WHERE u.id = :id")
=======
    @Query("UPDATE User u SET u.userName = :userName, u.userType = :userType, u.email = :email, u.password = :password, u.phoneNumber = :phoneNumber WHERE u.id = :id")
>>>>>>> main
    void updateUserById(@Param("id") int id, @Param("userName") String userName, @Param("email") String email, @Param("password") String password, @Param("phoneNumber") String phoneNumber);

    User findByuserNameAndPassword(String userName, String password);




}

