package najah.stu.service;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
class CustomerServiceTest {
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
             customerService = new CustomerService();
    }

    @Test
    void newServiceShouldNotBeLoggedIn() {
          assertFalse(customerService.isLoggedIn());
          assertNull(customerService.getLoggedCustomer());
    }

     @Test
    void loginWithWrongUsernameShouldFail() {
        boolean result = customerService.login("notARealUser", "somePassword");
        assertFalse(result);
        assertFalse(customerService.isLoggedIn());
        }

    @Test
    void requireLoginShouldThrowWhenNotLoggedIn() {
        assertThrows(IllegalStateException.class, () -> {
            customerService.requireLogin();
        } 
        
        		);
    }

    @Test
    void logoutShouldClearLoggedCustomerEvenIfNotLoggedIn() {
        customerService.logout();
        assertFalse(customerService.isLoggedIn());
        assertNull(customerService.getLoggedCustomer());
    }

   
    
    
}