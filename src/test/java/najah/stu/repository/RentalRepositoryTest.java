package najah.stu.repository;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import najah.stu.domain.Rental;
import najah.stu.domain.RentalStatus;
class RentalRepositoryTest {

    private RentalRepository rentalRepository;

    @BeforeEach
    void setUp() {
        rentalRepository = new RentalRepository();
    }
    @Test
    void saveShouldAssignIdWhenRentalIdIsZero() {
        Rental rental = new Rental(0, 1, "Ali", LocalDate.now(), LocalDate.now().plusDays(3));

        Rental saved = rentalRepository.save(rental);

        assertTrue(saved.getId() > 0);
    }
    @Test
    void saveShouldKeepExistingIdWhenNotZero() {
        Rental rental = new Rental(99, 1, "Ali", LocalDate.now(), LocalDate.now().plusDays(3));
        Rental saved = rentalRepository.save(rental);
        assertEquals(99, saved.getId());
    }

    @Test
    void getAllRentalsShouldReturnEmptyListInitially() {
        List<Rental> result = rentalRepository.getAllRentals();
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllRentalsShouldReturnSavedRentals() {
        Rental rental = new Rental(0, 1, "Ali", LocalDate.now(), LocalDate.now().plusDays(3));
        rentalRepository.save(rental);

        
        
        
        
        
        List<Rental> result = rentalRepository.getAllRentals();

        
        
        
        assertEquals(1, result.size()
        		);
    }

    @Test
    void findByIdShouldReturnRentalWhenItExists()
    {
        Rental rental = new Rental(0, 1, "Ali", LocalDate.now(), LocalDate.now().plusDays(3)
        		);
        Rental saved = rentalRepository.save(rental);

        Optional<Rental> result = rentalRepository.findById(saved.getId()
        		);

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId()
        		);
    }
    @Test
    void findByIdShouldReturnEmptyWhenNotFound() 
    {
        Optional<Rental> result = rentalRepository.findById(12345);

        assertTrue(result.isEmpty()
        		);
    }
    @Test
    void existsActiveRentalForVehicleShouldReturnTrueWhenActiveRentalExists() 
    {
        Rental rental = new Rental(0, 5, "Ali", LocalDate.now(), LocalDate.now().plusDays(3));
        rentalRepository.save(rental);
        boolean result = rentalRepository.existsActiveRentalForVehicle(5);
        assertTrue(result);
    }
    @Test
    void existsActiveRentalForVehicleShouldReturnFalseWhenNoRentalExists()
    {
        boolean result = rentalRepository.existsActiveRentalForVehicle(999);
        assertFalse(result);
    }
    @Test
    void existsActiveRentalForVehicleShouldReturnFalseWhenRentalIsReturned() {
        Rental rental = new Rental(0, 5, "Ali", LocalDate.now(), LocalDate.now().plusDays(3)
        		);
        rental.setStatus(RentalStatus.RETURNED);
        rentalRepository.save(rental);
        boolean result = rentalRepository.existsActiveRentalForVehicle(5);
        assertFalse(result);
    }
    @Test
    void findByStatusShouldReturnOnlyMatchingRentals() {
        Rental active = new Rental(0, 1, "Ali", LocalDate.now(), LocalDate.now().plusDays(3)
        		);
        Rental returned = new Rental(0, 2, "Sara", LocalDate.now(), LocalDate.now().plusDays(3)
        		);
        returned.setStatus(RentalStatus.RETURNED);
        rentalRepository.save(active);
        rentalRepository.save(returned);
        List<Rental> activeResults = rentalRepository.findByStatus(RentalStatus.ACTIVE);
        List<Rental> returnedResults = rentalRepository.findByStatus(RentalStatus.RETURNED);
        assertEquals(1, activeResults.size()
        		);
        assertEquals(1, returnedResults.size()
        		);
    }
}