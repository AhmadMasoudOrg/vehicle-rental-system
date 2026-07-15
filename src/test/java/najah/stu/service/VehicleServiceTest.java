package najah.stu.service;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import najah.stu.domain.Vehicle;
import najah.stu.repository.VehicleRepository;

class VehicleServiceTest {

    // simple fake repository so we don't need a real database/file for this test
    private static class FakeVehicleRepository extends VehicleRepository {
        private final List<Vehicle> vehicles;

        FakeVehicleRepository(List<Vehicle> vehicles) {
            this.vehicles = vehicles;
        }

        @Override
        public List<Vehicle> getAllVehicles() {
            return vehicles;
        }
    }

    @Test
    void getAvailableVehiclesShouldReturnEmptyListWhenRepositoryIsEmpty() {
        VehicleService service = new VehicleService(new FakeVehicleRepository(new ArrayList<>()));
        List<Vehicle> result = service.getAvailableVehicles();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAvailableVehiclesShouldOnlyReturnAvailableOnes() {
        Vehicle available1 = new Vehicle(1, "Toyota", "Corolla", true);
        Vehicle rented = new Vehicle(2, "Honda", "Civic", false);
        Vehicle available2 = new Vehicle(3, "Kia", "Sportage", true);
        List<Vehicle> allVehicles = new ArrayList<>();
        allVehicles.add(available1);
        allVehicles.add(rented);
        allVehicles.add(available2);
        VehicleService service = new VehicleService(new FakeVehicleRepository(allVehicles));
        List<Vehicle> result = service.getAvailableVehicles();
        assertEquals(2, result.size());
        assertTrue(result.contains(available1));
        assertTrue(result.contains(available2));
        assertFalse(result.contains(rented));
    }

    
    
    
    
    @Test
    void getAvailableVehiclesShouldReturnEmptyListWhenAllVehiclesAreRented() {
        Vehicle rented1 = new Vehicle(1, "Toyota", "Corolla", false);
        Vehicle rented2 = new Vehicle(2, "Honda", "Civic", false);

        List<Vehicle> allVehicles = new ArrayList<>();
        allVehicles.add(rented1);
        allVehicles.add(rented2);

        VehicleService service = new VehicleService(new FakeVehicleRepository(allVehicles));

        List<Vehicle> result = service.getAvailableVehicles();

        assertTrue(result.isEmpty());
    }
    @Test
    void getAvailableVehiclesShouldMarkVehicleUnavailableAfterMarkAsRented() {
        Vehicle vehicle = new Vehicle(1, "Toyota", "Corolla", true);
        vehicle.markAsRented();
        List<Vehicle> allVehicles = new ArrayList<>();
        allVehicles.add(vehicle);
        VehicleService service = new VehicleService(new FakeVehicleRepository(allVehicles));
        List<Vehicle> result = service.getAvailableVehicles();
        assertTrue(result.isEmpty());
    }
}