package najah.stu.repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import najah.stu.domain.Vehicle;






public class VehicleRepository {

    private List<Vehicle> vehicles;

    public VehicleRepository() {
        vehicles = new ArrayList<>();

        vehicles.add(new Vehicle(1, "Toyota", "Corolla", true));
        vehicles.add(new Vehicle(2, "Hyundai", "Tucson", true));
        vehicles.add(new Vehicle(3, "Kia", "Sportage", false));
        vehicles.add(new Vehicle(4, "BMW", "X5", true));
        vehicles.add(new Vehicle(5, "Mercedes", "C200", false));
    }

    public List<Vehicle> getAllVehicles() {
        return vehicles;
    }

    public Optional<Vehicle> findById(int id) {
        return vehicles.stream()
                .filter(vehicle -> vehicle.getId() == id)
                .findFirst();
    }
}