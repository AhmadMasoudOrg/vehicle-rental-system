package najah.stu.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import najah.stu.domain.Rental;
import najah.stu.domain.RentalStatus;

/**
 * Stores and manages rental records.
 *
 * Rental data is saved in a text file so it can be restored
 * when the application starts again.
 */
public class RentalRepository {

    private static final String FILE_PATH = "data/rentals.txt";

    private final List<Rental> rentals;
    private final AtomicInteger idGenerator;

    /**
     * Creates a repository and loads rental data from the file.
     */
    public RentalRepository() {

        this(true);
    }

    /**
     * Creates a repository and optionally loads rental data from the file.
     *
     * This constructor is useful for tests that need an empty repository.
     *
     * @param loadFromFile true to load rentals from the file
     */
    public RentalRepository(boolean loadFromFile) {

        rentals = new ArrayList<>();

        idGenerator = new AtomicInteger(1);

        if (loadFromFile) {

            loadRentals();

            updateIdGenerator();
        }
    }

    /**
     * Loads rental records from the rental data file.
     *
     * The method supports the old file format without a return date
     * and the new file format that includes a return date.
     */
    private void loadRentals() {

        File file = new File(FILE_PATH);

        if (!file.exists()) {
            createRentalFile(file);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] data = line.split(",");

                if (data.length != 6 && data.length != 7) {
                    continue;
                }

                int id = Integer.parseInt(data[0].trim());

                int vehicleId = Integer.parseInt(data[1].trim());

                String customerName = data[2].trim();

                LocalDate startDate = LocalDate.parse(data[3].trim());

                LocalDate endDate = LocalDate.parse(data[4].trim());

                LocalDate returnDate = null;

                RentalStatus status;

                if (data.length == 7) {

                    if (!data[5].trim().equals("null")) {
                        returnDate = LocalDate.parse(data[5].trim());
                    }

                    status = RentalStatus.valueOf(data[6].trim());

                } else {

                    status = RentalStatus.valueOf(data[5].trim());
                }

                Rental rental = new Rental(
                        id,
                        vehicleId,
                        customerName,
                        startDate,
                        endDate,
                        returnDate,
                        status
                );

                rentals.add(rental);
            }

        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }
    }

    /**
     * Creates the rental data file and its parent directory
     * when they do not exist.
     *
     * @param file rental data file
     */
    private void createRentalFile(File file) {

        File directory = file.getParentFile();

        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }

        try {

            file.createNewFile();

        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }
    }

    /**
     * Updates the ID generator based on the highest stored rental ID.
     */
    private void updateIdGenerator() {

        int maxId = 0;

        for (Rental rental : rentals) {

            if (rental.getId() > maxId) {
                maxId = rental.getId();
            }
        }

        idGenerator.set(maxId + 1);
    }

    /**
     * Saves a rental in the repository.
     *
     * A new ID is generated when the rental ID is zero.
     *
     * @param rental rental to save
     * @return the saved rental
     */
    public Rental save(Rental rental) {

        if (rental.getId() == 0) {
            rental.setId(idGenerator.getAndIncrement());
        }

        if (!rentals.contains(rental)) {
            rentals.add(rental);
        }

        saveChanges();

        return rental;
    }

    /**
     * Writes all rental records to the rental data file.
     */
    public void saveChanges() {

        File file = new File(FILE_PATH);

        if (!file.exists()) {
            createRentalFile(file);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            for (Rental rental : rentals) {

                writer.write(
                        rental.getId()
                                + ","
                                + rental.getVehicleId()
                                + ","
                                + rental.getCustomerName()
                                + ","
                                + rental.getStartDate()
                                + ","
                                + rental.getEndDate()
                                + ","
                                + rental.getReturnDate()
                                + ","
                                + rental.getStatus()
                );

                writer.newLine();
            }

        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }
    }

    /**
     * Returns all rental records.
     *
     * @return list of all rentals
     */
    public List<Rental> getAllRentals() {

        return rentals;
    }

    /**
     * Finds a rental by its ID.
     *
     * @param id rental ID
     * @return rental when found, otherwise an empty optional
     */
    public Optional<Rental> findById(int id) {

        return rentals.stream()
                .filter(rental -> rental.getId() == id)
                .findFirst();
    }

    /**
     * Finds an active rental for a specific vehicle.
     *
     * @param vehicleId vehicle ID
     * @return active rental when found, otherwise an empty optional
     */
    public Optional<Rental> findActiveRentalByVehicleId(int vehicleId) {

        return rentals.stream()
                .filter(rental -> rental.getVehicleId() == vehicleId)
                .filter(Rental::isActive)
                .findFirst();
    }

    /**
     * Checks whether a vehicle already has an active rental.
     *
     * @param vehicleId vehicle ID
     * @return true when an active rental exists
     */
    public boolean existsActiveRentalForVehicle(int vehicleId) {

        return findActiveRentalByVehicleId(vehicleId).isPresent();
    }

    /**
     * Finds all rentals with the specified status.
     *
     * @param status rental status
     * @return rentals matching the specified status
     */
    public List<Rental> findByStatus(RentalStatus status) {

        List<Rental> result = new ArrayList<>();

        for (Rental rental : rentals) {

            if (rental.getStatus() == status) {
                result.add(rental);
            }
        }

        return result;
    }
}