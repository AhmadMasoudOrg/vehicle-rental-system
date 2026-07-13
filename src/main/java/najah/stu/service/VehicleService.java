package najah.stu.service;

import java.util.ArrayList;
import java.util.List;

import najah.stu.domain.Vehicle;
import najah.stu.repository.VehicleRepository;

public class VehicleService {

    private final VehicleRepository repository;

    public VehicleService(VehicleRepository repository) {
        this.repository = repository;
    }

    public List<Vehicle> getAvailableVehicles() {

        List<Vehicle> availableVehicles = new ArrayList<>();

        for (Vehicle vehicle : repository.getAllVehicles()) {
            if (vehicle.isAvailable()) {
                availableVehicles.add(vehicle);
            }
        }

        return availableVehicles;
    }
}