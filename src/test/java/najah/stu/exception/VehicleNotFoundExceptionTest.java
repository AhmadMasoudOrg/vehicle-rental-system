package najah.stu.exception;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class VehicleNotFoundExceptionTest {
    @Test
    void constructorShouldSetMessageCorrectly() {
        VehicleNotFoundException exception =
                new VehicleNotFoundException("Vehicle not found.");

        assertEquals("Vehicle not found.", exception.getMessage());
    }
    @Test
    void exceptionShouldBeInstanceOfRuntimeException() {
        VehicleNotFoundException exception =
                new VehicleNotFoundException("Vehicle not found.");

        assertTrue(exception instanceof RuntimeException);
    }
    @Test
    void throwingExceptionShouldBeCatchableAndCarryMessage() {
        Exception exception = assertThrows(VehicleNotFoundException.class, () -> {
            throw new VehicleNotFoundException("Vehicle not found with id: 42");
        }
        
        		
        		
        		
        		);

        assertEquals("Vehicle not found with id: 42", exception.getMessage()
        		);
   
    }
    
    
    
}