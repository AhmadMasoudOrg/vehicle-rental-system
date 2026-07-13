package najah.stu.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import najah.stu.domain.Vehicle;

public class VehicleRepository {

    private static final String FILE_PATH = "data/vehicles.txt";

    private final List<Vehicle> vehicles;

    public VehicleRepository() {
        vehicles = new ArrayList<>();
        loadVehicles();
    }

    private void loadVehicles() {

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(FILE_PATH))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] data = line.split(",");

                if (data.length != 4) {
                    System.out.println("Invalid vehicle data: " + line);
                    continue;
                }

                int id = Integer.parseInt(data[0].trim());
                String brand = data[1].trim();
                String model = data[2].trim();
                boolean available = Boolean.parseBoolean(data[3].trim());

                vehicles.add(new Vehicle(id, brand, model, available));
            }

        } catch (IOException | NumberFormatException exception) {

            System.out.println("Error reading vehicles file: " + exception.getMessage());
        }
    }

    public List<Vehicle> getAllVehicles() {
        return vehicles;
    }

    public Optional<Vehicle> findById(int id) {

        return vehicles.stream().filter(vehicle -> vehicle.getId() == id).findFirst();
    }

    public void saveChanges() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {

            for (Vehicle vehicle : vehicles) {
                writer.write(vehicle.getId() + ","+ vehicle.getBrand() + ","+ vehicle.getModel() + ","+ vehicle.isAvailable());
                writer.newLine();
            }

        } catch (IOException exception) {

            throw new RuntimeException("Could not save vehicle status.", exception);
        }
    }
}