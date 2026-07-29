package najah.stu.domain;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class UserTest {
    @Test
    void constructorShouldSetFieldsCorrectly()
    {
        User user = new Customer(1, "ali123", "pass123", "ali@example.com");

        assertEquals(1, user.getId()
        		
        		
        		);
        assertEquals("ali123", user.getUsername());
        assertEquals("pass123", user.getPassword());
        assertEquals("Customer", user.getRole());
    }
    @Test
    void settersShouldUpdateFieldsCorrectly() 
    {
        User user = new Customer(1, "ali123", "pass123", "ali@example.com");
        user.setId(2);
        user.setUsername("newUsername");
        user.setPassword("newPassword");
        user.setRole("Admin");
        assertEquals(2, user.getId());
        assertEquals("newUsername", user.getUsername());
        assertEquals("newPassword", user.getPassword());
        assertEquals("Admin", user.getRole()
        		
        		
        		
        		
        		);
    }
    @Test
    void noArgConstructorShouldCreateUserWithDefaultValues() {
        User user = new Customer(0, null, null, null);

        assertEquals(0, user.getId()
        		);
        assertNull(user.getUsername()
        		);
    }
}