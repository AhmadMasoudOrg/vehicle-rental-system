package najah.stu.repository;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import najah.stu.domain.Vehicle;
class VehicleRepositoryTest {
    private VehicleRepository vehicleRepository;

    
    
    
    
    @BeforeEach
    void setUp() {
        vehicleRepository = new VehicleRepository();
    }

    
    
    
    @Test
    void getAllVehiclesShouldNotBeNull() {
        List<Vehicle> result = vehicleRepository.getAllVehicles();
        assertNotNull(result);
    }

    
    
    
    
    @Test
    
    void findByIdShouldReturnEmptyWhenVehicleDoesNotExist() {
        Optional<Vehicle> result = vehicleRepository.findById(999999);
        assertTrue(result.isEmpty());
    }

    
}