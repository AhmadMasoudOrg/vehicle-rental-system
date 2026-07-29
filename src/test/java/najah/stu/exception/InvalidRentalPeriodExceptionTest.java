package najah.stu.exception;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class InvalidRentalPeriodExceptionTest {
    @Test
    void constructorShouldSetMessageCorrectly() {
        InvalidRentalPeriodException exception =
                new InvalidRentalPeriodException("Rental period is invalid.");

        assertEquals("Rental period is invalid.", exception.getMessage()
        		);
    }
    @Test
    void exceptionShouldBeInstanceOfRuntimeException() 
    {
        InvalidRentalPeriodException exception =
                new InvalidRentalPeriodException("Rental period is invalid.");

        assertTrue(exception instanceof RuntimeException);
    }
    @Test
    void throwingExceptionShouldBeCatchableAndCarryMessage() {
        Exception exception = assertThrows(InvalidRentalPeriodException.class, () -> {
            throw new InvalidRentalPeriodException("Start date cannot be in the past.");
        }
        
        		
        		
        		);

       
        assertEquals("Start date cannot be in the past.", exception.getMessage());
   
    
    
    


    }
}