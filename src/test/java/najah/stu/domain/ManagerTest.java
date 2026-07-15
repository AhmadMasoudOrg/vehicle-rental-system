package najah.stu.domain;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class ManagerTest {
    @Test
    void constructorShouldSetAllFieldsCorrectly() {
        Manager manager = new Manager(1, "admin1", "adminPass");
        assertEquals(1, manager.getId());
        assertEquals("admin1", manager.getUsername());
        assertEquals("adminPass", manager.getPassword());
        assertEquals("Manager", manager.getRole());
    }

    
    
    
    @Test
    void managerShouldBeInstanceOfUser() {
        Manager manager = new Manager(1, "admin1", "adminPass");
        assertTrue(manager instanceof User);
    }
    @Test
    
    void settersInheritedFromUserShouldWork() {
        Manager manager = new Manager(1, "admin1", "adminPass");
        manager.setUsername("newAdmin");
        manager.setPassword("newPass");
        assertEquals("newAdmin", manager.getUsername());
        assertEquals("newPass", manager.getPassword());
  
    
    }
    
    
    
}