package najah.stu.service;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
class ManagerServiceTest {

    private ManagerService managerService;
    @BeforeEach
    void setUp() {
        managerService = new ManagerService();
    }

    @Test
    void newServiceShouldNotBeLoggedIn() {
        assertFalse(managerService.isLoggedIn()  );
        assertNull(managerService.getLoggedInManager()  );
    }
    @Test
    void loginWithNullUsernameShouldReturnFalse() {
        boolean result = managerService.login(null, "somePassword");
        assertFalse(result);
    }
    @Test
    void loginWithNullPasswordShouldReturnFalse() {
        boolean result = managerService.login("someUsername", null);
        assertFalse(result);
    }
    @Test
    void loginWithWrongUsernameShouldFail() {
        boolean result = managerService.login("notARealManager", "wrongPassword");
        assertFalse(result);
        assertFalse(managerService.isLoggedIn() );
    }
  
    
    
    
    @Test
    void requireLoginShouldThrowWhenNotLoggedIn() {
        assertThrows(IllegalStateException.class, () -> {
            managerService.requireLogin();
        }
        
        		
        		
        		
        		
        		);
    }

    @Test
    void logoutShouldClearLoggedInManager() {
        managerService.logout();
        assertFalse(managerService.isLoggedIn());
        assertNull(managerService.getLoggedInManager());
    }
}