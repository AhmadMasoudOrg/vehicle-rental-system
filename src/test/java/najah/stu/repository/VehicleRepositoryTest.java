package najah.stu.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import najah.stu.domain.Car;
import najah.stu.domain.SUV;
import najah.stu.domain.Truck;
import najah.stu.domain.Vehicle;

class VehicleRepositoryTest {

    private static final Path FILE_PATH = Paths.get("data/vehicles.txt");
    private static final Path DIR_PATH = Paths.get("data");
    private byte[] originalContent;
    private boolean fileExisted;

    @BeforeEach
    void setUp() throws IOException {
        fileExisted = Files.exists(FILE_PATH);
        if (fileExisted) {
            originalContent = Files.readAllBytes(FILE_PATH);
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        if (!Files.exists(DIR_PATH)) {
            Files.createDirectories(DIR_PATH);
        }
        if (fileExisted) {
            Files.write(FILE_PATH, originalContent);
        } else if (Files.exists(FILE_PATH)) {
            Files.delete(FILE_PATH);
        }
    }

    private void writeFile(String content) throws IOException {
        Files.createDirectories(DIR_PATH);
        Files.write(FILE_PATH, content.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void loadVehiclesShouldLoadCarWhenTypeMissing() throws IOException {
        writeFile("1,Toyota,Corolla,true,50.0");
        VehicleRepository repository = new VehicleRepository();
        List<Vehicle> vehicles = repository.getAllVehicles();
        assertEquals(1, vehicles.size());
        assertTrue(vehicles.get(0) instanceof Car);
    }

    @Test
    void loadVehiclesShouldLoadSuvWhenTypeSuv() throws IOException {
        writeFile("2,Ford,Explorer,true,80.0,SUV");
        VehicleRepository repository = new VehicleRepository();
        List<Vehicle> vehicles = repository.getAllVehicles();
        assertEquals(1, vehicles.size());
        assertTrue(vehicles.get(0) instanceof SUV);
    }

    @Test
    void loadVehiclesShouldLoadTruckWhenTypeTruck() throws IOException {
        writeFile("3,Volvo,FH16,false,120.0,TRUCK");
        VehicleRepository repository = new VehicleRepository();
        List<Vehicle> vehicles = repository.getAllVehicles();
        assertEquals(1, vehicles.size());
        assertTrue(vehicles.get(0) instanceof Truck);
    }

    @Test
    void loadVehiclesShouldSkipBlankLines() throws IOException {
        writeFile("\n\n");
        VehicleRepository repository = new VehicleRepository();
        assertEquals(0, repository.getAllVehicles().size());
    }

    @Test
    void loadVehiclesShouldSkipLineWithInvalidFieldCount() throws IOException {
        writeFile("1,Toyota,Corolla");
        VehicleRepository repository = new VehicleRepository();
        assertEquals(0, repository.getAllVehicles().size());
    }

    @Test
    void loadVehiclesShouldStopAfterInvalidNumberFormat() throws IOException {
        writeFile("1,Toyota,Corolla,true,50.0\nabc,Ford,Focus,true,40.0");
        VehicleRepository repository = new VehicleRepository();
        assertEquals(1, repository.getAllVehicles().size());
    }

    @Test
    void loadVehiclesShouldStopAfterInvalidVehicleType() throws IOException {
        writeFile("1,Toyota,Corolla,true,50.0\n2,Ford,Focus,true,40.0,BOAT");
        VehicleRepository repository = new VehicleRepository();
        assertEquals(1, repository.getAllVehicles().size());
    }

    @Test
    void loadVehiclesShouldHandleMissingFileGracefully() throws IOException {
        Files.createDirectories(DIR_PATH);
        if (Files.exists(FILE_PATH)) {
            Files.delete(FILE_PATH);
        }
        VehicleRepository repository = new VehicleRepository();
        assertEquals(0, repository.getAllVehicles().size());
    }

    @Test
    void findByIdShouldReturnVehicleWhenExists() throws IOException {
        writeFile("5,Honda,Civic,true,60.0");
        VehicleRepository repository = new VehicleRepository();
        Optional<Vehicle> result = repository.findById(5);
        assertTrue(result.isPresent());
        assertEquals("Honda", result.get().getBrand());
    }

    @Test
    void findByIdShouldReturnEmptyWhenNotExists() throws IOException {
        writeFile("5,Honda,Civic,true,60.0");
        VehicleRepository repository = new VehicleRepository();
        Optional<Vehicle> result = repository.findById(999);
        assertFalse(result.isPresent());
    }

    @Test
    void saveChangesShouldWriteVehicleDataToFile() throws IOException {
        writeFile("6,Kia,Sportage,true,70.0,SUV");
        VehicleRepository repository = new VehicleRepository();
        repository.saveChanges();
        String content = Files.readString(FILE_PATH);
        assertTrue(content.contains("Kia"));
        assertTrue(content.contains("Sportage"));
        assertTrue(content.contains("SUV"));
    }

    @Test
    void saveChangesShouldThrowRuntimeExceptionWhenDirectoryMissing() throws IOException {
        writeFile("7,Mazda,CX5,true,65.0");
        VehicleRepository repository = new VehicleRepository();
        deleteDirectoryRecursively(DIR_PATH.toFile());
        assertThrows(RuntimeException.class, repository::saveChanges);
    }

    private void deleteDirectoryRecursively(File file) {
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                deleteDirectoryRecursively(child);
            }
        }
        file.delete();
    }
}
