package najah.stu.service;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
class RentalServiceTest
{

    private RentalService rentalService;

    @BeforeEach
    void setUp() 
    
    
    
    {
        rentalService = new RentalService();
    }

    @Test
    void rentVehicleShouldThrowWhenCustomerNotLoggedIn() {
        assertThrows(IllegalStateException.class, () -> {
            rentalService.rentVehicle(1, "Ali", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
        }
        
        		
        		);
    }

    
    
    @Test
    void returnVehicleShouldThrowWhenCustomerNotLoggedIn() {
        assertThrows(IllegalStateException.class, () -> {
            rentalService.returnVehicle(1);
        }
        
        		
        		
        		
        		);
    }

   
}