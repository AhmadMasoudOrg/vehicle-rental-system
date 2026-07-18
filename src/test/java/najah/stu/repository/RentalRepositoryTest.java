package najah.stu.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import najah.stu.domain.Rental;
import najah.stu.domain.RentalStatus;

class RentalRepositoryTest {

    private static final Path FILE_PATH = Path.of("data/rentals.txt");

    private RentalRepository rentalRepository;

    private String originalFileContent;
    private boolean fileExisted;

    @BeforeEach
    void setUp() throws IOException {

        Files.createDirectories(FILE_PATH.getParent());

        fileExisted = Files.exists(FILE_PATH);

        if (fileExisted) {
            originalFileContent = Files.readString(FILE_PATH);
        }

        Files.writeString(FILE_PATH,"");

        rentalRepository = new RentalRepository(false);
    }

    @AfterEach
    void tearDown() throws IOException {

        if (fileExisted) {
            Files.writeString(FILE_PATH,originalFileContent);
        } else {
            Files.deleteIfExists(FILE_PATH);
        }
    }

    @Test
    void saveShouldAssignIdWhenRentalIdIsZero() {

        Rental rental = new Rental(
                0,
                1,
                "Ali",
                LocalDate.now(),
                LocalDate.now().plusDays(3)
        );

        Rental saved = rentalRepository.save(rental);

        assertTrue(saved.getId() > 0);
    }

    @Test
    void saveShouldKeepExistingIdWhenNotZero() {

        Rental rental = new Rental(
                99,
                1,
                "Ali",
                LocalDate.now(),
                LocalDate.now().plusDays(3)
        );

        Rental saved = rentalRepository.save(rental);

        assertEquals(99,saved.getId());
    }

    @Test
    void saveShouldWriteRentalToFile() throws IOException {

        Rental rental = new Rental(
                0,
                1,
                "Ali",
                LocalDate.of(2026,7,20),
                LocalDate.of(2026,7,23)
        );

        rentalRepository.save(rental);

        String fileContent = Files.readString(FILE_PATH);

        assertTrue(fileContent.contains("Ali"));
        assertTrue(fileContent.contains("2026-07-20"));
        assertTrue(fileContent.contains("2026-07-23"));
        assertTrue(fileContent.contains("ACTIVE"));
    }

    @Test
    void getAllRentalsShouldReturnEmptyListInitially() {

        List<Rental> result = rentalRepository.getAllRentals();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllRentalsShouldReturnSavedRentals() {

        Rental rental = new Rental(
                0,
                1,
                "Ali",
                LocalDate.now(),
                LocalDate.now().plusDays(3)
        );

        rentalRepository.save(rental);

        List<Rental> result = rentalRepository.getAllRentals();

        assertEquals(1,result.size());
    }

    @Test
    void findByIdShouldReturnRentalWhenItExists() {

        Rental rental = new Rental(
                0,
                1,
                "Ali",
                LocalDate.now(),
                LocalDate.now().plusDays(3)
        );

        Rental saved = rentalRepository.save(rental);

        Optional<Rental> result = rentalRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(saved.getId(),result.get().getId());
    }

    @Test
    void findByIdShouldReturnEmptyWhenNotFound() {

        Optional<Rental> result = rentalRepository.findById(12345);

        assertTrue(result.isEmpty());
    }

    @Test
    void findActiveRentalByVehicleIdShouldReturnActiveRental() {

        Rental rental = new Rental(
                0,
                5,
                "Ali",
                LocalDate.now(),
                LocalDate.now().plusDays(3)
        );

        rentalRepository.save(rental);

        Optional<Rental> result = rentalRepository.findActiveRentalByVehicleId(5);

        assertTrue(result.isPresent());
        assertEquals(5,result.get().getVehicleId());
    }

    @Test
    void findActiveRentalByVehicleIdShouldIgnoreReturnedRental() {

        Rental rental = new Rental(
                0,
                5,
                "Ali",
                LocalDate.now(),
                LocalDate.now().plusDays(3)
        );

        rental.setStatus(RentalStatus.RETURNED);

        rentalRepository.save(rental);

        Optional<Rental> result = rentalRepository.findActiveRentalByVehicleId(5);

        assertTrue(result.isEmpty());
    }

    @Test
    void existsActiveRentalForVehicleShouldReturnTrueWhenActiveRentalExists() {

        Rental rental = new Rental(
                0,
                5,
                "Ali",
                LocalDate.now(),
                LocalDate.now().plusDays(3)
        );

        rentalRepository.save(rental);

        boolean result = rentalRepository.existsActiveRentalForVehicle(5);

        assertTrue(result);
    }

    @Test
    void existsActiveRentalForVehicleShouldReturnFalseWhenNoRentalExists() {

        boolean result = rentalRepository.existsActiveRentalForVehicle(999);

        assertFalse(result);
    }

    @Test
    void existsActiveRentalForVehicleShouldReturnFalseWhenRentalIsReturned() {

        Rental rental = new Rental(
                0,
                5,
                "Ali",
                LocalDate.now(),
                LocalDate.now().plusDays(3)
        );

        rental.setStatus(RentalStatus.RETURNED);

        rentalRepository.save(rental);

        boolean result = rentalRepository.existsActiveRentalForVehicle(5);

        assertFalse(result);
    }

    @Test
    void findByStatusShouldReturnOnlyMatchingRentals() {

        Rental active = new Rental(
                0,
                1,
                "Ali",
                LocalDate.now(),
                LocalDate.now().plusDays(3)
        );

        Rental returned = new Rental(
                0,
                2,
                "Sara",
                LocalDate.now(),
                LocalDate.now().plusDays(3)
        );

        returned.setStatus(RentalStatus.RETURNED);

        rentalRepository.save(active);
        rentalRepository.save(returned);

        List<Rental> activeResults = rentalRepository.findByStatus(RentalStatus.ACTIVE);

        List<Rental> returnedResults = rentalRepository.findByStatus(RentalStatus.RETURNED);

        assertEquals(1,activeResults.size());
        assertEquals(1,returnedResults.size());
    }

    @Test
    void constructorShouldLoadOldRentalFileFormat() throws IOException {

        Files.writeString(
                FILE_PATH,
                "7,3,Ali,2026-07-20,2026-07-25,ACTIVE"
        );

        RentalRepository repository = new RentalRepository();

        List<Rental> rentals = repository.getAllRentals();

        assertEquals(1,rentals.size());
        assertEquals(7,rentals.get(0).getId());
        assertEquals(3,rentals.get(0).getVehicleId());
        assertEquals("Ali",rentals.get(0).getCustomerName());
        assertEquals(RentalStatus.ACTIVE,rentals.get(0).getStatus());
        assertNull(rentals.get(0).getReturnDate());
    }

    @Test
    void constructorShouldLoadNewRentalFileFormat() throws IOException {

        Files.writeString(
                FILE_PATH,
                "8,4,Sara,2026-07-20,2026-07-25,2026-07-27,RETURNED"
        );

        RentalRepository repository = new RentalRepository();

        List<Rental> rentals = repository.getAllRentals();

        assertEquals(1,rentals.size());
        assertEquals(8,rentals.get(0).getId());
        assertEquals(4,rentals.get(0).getVehicleId());
        assertEquals("Sara",rentals.get(0).getCustomerName());
        assertEquals(LocalDate.of(2026,7,27),rentals.get(0).getReturnDate());
        assertEquals(RentalStatus.RETURNED,rentals.get(0).getStatus());
    }

    @Test
    void constructorShouldLoadNullReturnDate() throws IOException {

        Files.writeString(
                FILE_PATH,
                "9,5,Omar,2026-07-20,2026-07-25,null,ACTIVE"
        );

        RentalRepository repository = new RentalRepository();

        Rental rental = repository.getAllRentals().get(0);

        assertNull(rental.getReturnDate());
        assertEquals(RentalStatus.ACTIVE,rental.getStatus());
    }

    @Test
    void constructorShouldIgnoreEmptyLines() throws IOException {

        Files.writeString(
                FILE_PATH,
                "\n\n10,6,Ahmad,2026-07-20,2026-07-25,ACTIVE\n\n"
        );

        RentalRepository repository = new RentalRepository();

        assertEquals(1,repository.getAllRentals().size());
    }

    @Test
    void constructorShouldIgnoreInvalidDataLength() throws IOException {

        Files.writeString(
                FILE_PATH,
                "invalid,rental,data\n"
                        + "11,7,Lina,2026-07-20,2026-07-25,ACTIVE"
        );

        RentalRepository repository = new RentalRepository();

        assertEquals(1,repository.getAllRentals().size());
        assertEquals("Lina",repository.getAllRentals().get(0).getCustomerName());
    }

    @Test
    void idGeneratorShouldContinueAfterHighestLoadedId() throws IOException {

        Files.writeString(
                FILE_PATH,
                "20,1,Ali,2026-07-20,2026-07-25,ACTIVE"
        );

        RentalRepository repository = new RentalRepository();

        Rental rental = new Rental(
                0,
                2,
                "Sara",
                LocalDate.of(2026,7,21),
                LocalDate.of(2026,7,26)
        );

        repository.save(rental);

        assertEquals(21,rental.getId());
    }

    @Test
    void saveChangesShouldSaveReturnDateAndReturnedStatus() throws IOException {

        Rental rental = new Rental(
                0,
                1,
                "Ali",
                LocalDate.of(2026,7,20),
                LocalDate.of(2026,7,25)
        );

        rental.setReturnDate(LocalDate.of(2026,7,27));
        rental.setStatus(RentalStatus.RETURNED);

        rentalRepository.save(rental);

        String fileContent = Files.readString(FILE_PATH);

        assertTrue(fileContent.contains("2026-07-27"));
        assertTrue(fileContent.contains("RETURNED"));
    }
}