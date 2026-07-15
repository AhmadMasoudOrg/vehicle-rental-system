package najah.stu.domain;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
class RentalTest {
    @Test
    void constructorShouldSetFieldsCorrectlyAndDefaultStatusToActive() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 1, 5);
        Rental rental = new Rental(1, 10, "Ali", start, end);
        assertEquals(1, rental.getId());
        assertEquals(10, rental.getVehicleId());
        assertEquals("Ali", rental.getCustomerName());
        assertEquals(start, rental.getStartDate());
        assertEquals(end, rental.getEndDate());
        assertEquals(RentalStatus.ACTIVE, rental.getStatus());
        assertTrue(rental.isActive());
    }
    @Test
    void settersShouldUpdateFieldsCorrectly() {
        Rental rental = new Rental(1, 10, "Ali", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 5));
        rental.setId(2);
        rental.setVehicleId(20);
        rental.setCustomerName("Sara");
        rental.setStartDate(LocalDate.of(2026, 2, 1));
        rental.setEndDate(LocalDate.of(2026, 2, 10));
        assertEquals(2, rental.getId());
        assertEquals(20, rental.getVehicleId());
        assertEquals("Sara", rental.getCustomerName());
        assertEquals(LocalDate.of(2026, 2, 1), rental.getStartDate());
        assertEquals(LocalDate.of(2026, 2, 10), rental.getEndDate());
    }
    @Test
    void setStatusToReturnedShouldMakeIsActiveFalse() {
        Rental rental = new Rental(1, 10, "Ali", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 5));

        rental.setStatus(RentalStatus.RETURNED);

        assertFalse(rental.isActive());
        assertEquals(RentalStatus.RETURNED, rental.getStatus());
    }
    @Test
    void getDurationInDaysShouldCalculateCorrectly() {
        Rental rental = new Rental(1, 10, "Ali", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 5));

        long duration = rental.getDurationInDays();

        assertEquals(4, duration);
    }
    @Test
    void getDurationInDaysShouldBeZeroWhenStartAndEndAreSame() {
        LocalDate sameDay = LocalDate.of(2026, 1, 1);
        Rental rental = new Rental(1, 10, "Ali", sameDay, sameDay);
        long duration = rental.getDurationInDays();

        assertEquals(0, duration);
    }
}