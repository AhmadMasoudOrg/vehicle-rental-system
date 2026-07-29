package najah.stu.repository;
import static org.junit.jupiter.api.Assertions.*;
import najah.stu.domain.Customer;
import org.junit.jupiter.api.Test;
class CustomerRepositoryTest {
	
    private CustomerRepository customerRepository = new CustomerRepository();

    
    
    
    
    @Test
    void findByUsernameShouldReturnNullWhenUsernameDoesNotExist() {
        Customer result = customerRepository.findByUsername("thisUsernameDoesNotExist999");

        assertNull(result);
    }

    
    
    
    @Test
    void findByUsernameShouldReturnNullForEmptyUsername() {
        Customer result = customerRepository.findByUsername("");
        assertNull(result);
    }
    @Test
    void findByUsernameShouldBeCaseInsensitive() {
        
    }
}