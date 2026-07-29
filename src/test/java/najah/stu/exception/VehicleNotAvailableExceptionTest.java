package najah.stu.exception;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class VehicleNotAvailableExceptionTest {
    @Test
    void constructorShouldSetMessageCorrectly() 
    {
        VehicleNotAvailableException exception =
                new VehicleNotAvailableException("Vehicle is already rented.");

        assertEquals("Vehicle is already rented.", exception.getMessage()
        		);
    }

    @Test
    void exceptionShouldBeInstanceOfRuntimeException() {
        VehicleNotAvailableException exception =
                new VehicleNotAvailableException("Vehicle is already rented.");
        assertTrue(exception instanceof RuntimeException);
    }
    @Test
    void throwingExceptionShouldBeCatchableAndCarryMessage() {
        Exception exception = assertThrows(VehicleNotAvailableException.class, () -> {
            throw new VehicleNotAvailableException("Vehicle with id 5 is already rented.");
        }
        
        		);
        assertEquals("Vehicle with id 5 is already rented.", exception.getMessage()
        		
        		
        		
        		);
   
    
    
    
    
    
    }
}