package najah.stu.domain;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class CustomerTest {

	
	
    @Test
    void constructorShouldSetAllFieldsCorrectly() 
    
    
    {
        Customer customer = new Customer(1, "ali123", "pass123", "ali@example.com");

        assertEquals(1, customer.getId());
        assertEquals("ali123", customer.getUsername());
        assertEquals("pass123", customer.getPassword());
        assertEquals("Customer", customer.getRole());
        assertEquals("ali@example.com", customer.getEmail());
    }

    
    
    
    @Test
    void setEmailShouldUpdateEmail() 
    
    
    {
        Customer customer = new Customer(1, "ali123", "pass123", "ali@example.com");

        customer.setEmail("newemail@example.com");

        assertEquals("newemail@example.com", customer.getEmail()
        		
        		
        		);
    }

    
    
    
    @Test
    void customerShouldBeInstanceOfUser() 
    
    
    {
        Customer customer = new Customer(1, "ali123", "pass123", "ali@example.com");

        assertTrue(customer instanceof User);
    }
}