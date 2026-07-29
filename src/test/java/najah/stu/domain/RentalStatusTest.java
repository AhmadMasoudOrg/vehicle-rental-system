package najah.stu.domain;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class RentalStatusTest {
    @Test
    void enumShouldHaveThreeValues() {
        RentalStatus[] values = RentalStatus.values();
        assertEquals(3, values.length);
    }
    @Test
    void valueOfShouldReturnCorrectEnumConstant() {
        assertEquals(RentalStatus.ACTIVE, RentalStatus.valueOf("ACTIVE"));
        assertEquals(RentalStatus.RETURNED, RentalStatus.valueOf("RETURNED"));
        assertEquals(RentalStatus.CANCELLED, RentalStatus.valueOf("CANCELLED"));
    }
    @Test
    void enumConstantsShouldNotBeEqualToEachOther() {
        assertNotEquals(RentalStatus.ACTIVE, RentalStatus.RETURNED);
        assertNotEquals(RentalStatus.RETURNED, RentalStatus.CANCELLED);
    }
    @Test
    void valueOfWithInvalidNameShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            RentalStatus.valueOf("NOT_A_REAL_STATUS");
        }
        
        		
        		);
    }
}