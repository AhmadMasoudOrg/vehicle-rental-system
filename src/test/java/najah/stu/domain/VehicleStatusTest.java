package najah.stu.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class VehicleStatusTest {

    @Test
    void enumShouldHaveTwoValues() {
        VehicleStatus[] values = VehicleStatus.values();
        assertEquals(2, values.length);
    }

    
    
    @Test
    void valueOfShouldReturnCorrectEnumConstant() {
        assertEquals(VehicleStatus.AVAILABLE, VehicleStatus.valueOf("AVAILABLE"));
        assertEquals(VehicleStatus.RENTED, VehicleStatus.valueOf("RENTED"));
    }
    @Test
    void enumConstantsShouldNotBeEqualToEachOther() {
        assertNotEquals(VehicleStatus.AVAILABLE, VehicleStatus.RENTED);
    }

    @Test
    void valueOfWithInvalidNameShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            VehicleStatus.valueOf("NOT_A_REAL_STATUS");
        }
        
        		
        		
        		
        		);
    }
    
}
