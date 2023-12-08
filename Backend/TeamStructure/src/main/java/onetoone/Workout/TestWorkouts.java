package onetoone.Workout;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import onetoone.users.User;
import onetoone.users.UserRepository;
import onetoone.users.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
public class TestWorkouts {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository repo;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void getUserByIdTest() {
        when(repo.findById(1)).thenReturn(new User("jdoe", "john","doe" ,"jdoe@gmail.com", "testpw","12345678"));

        User user = userService.findByID(1);

        assertEquals("john", user.getFirstName());
        assertEquals("testpw", user.getPassword());
        assertEquals("jdoe@gmail.com", user.getEmail());

    }

}
