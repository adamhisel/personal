package onetoone.Admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findById(int id);

    @Transactional
    void deleteById(int id);

    @Modifying
    @Query("UPDATE Admin a SET a.userName = :userName, a.userType = :userType, a.email = :email, a.password = :password, a.phoneNumber = :phoneNumber WHERE a.id = :id")
    void updateAdminById(@Param("id") int id, @Param("userName") String userName, @Param("userType") String userType, @Param("email") String email, @Param("password") String password, @Param("phoneNumber") String phoneNumber);

}