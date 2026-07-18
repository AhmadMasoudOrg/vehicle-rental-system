package najah.stu.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import najah.stu.domain.Rental;
import najah.stu.domain.RentalStatus;
import najah.stu.domain.Vehicle;
import najah.stu.exception.InvalidRentalPeriodException;
import najah.stu.exception.VehicleNotAvailableException;
import najah.stu.exception.VehicleNotFoundException;
import najah.stu.repository.RentalRepository;
import najah.stu.repository.VehicleRepository;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private Vehicle vehicle;

    private RentalService rentalService;

    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        rentalService = new RentalService(
                vehicleRepository,
                rentalRepository,
                customerService
        );

        startDate = LocalDate.now().plusDays(1);
        endDate = startDate.plusDays(4);
    }

    @Test
    void rentVehicle_shouldRentVehicleSuccessfully() {
        int vehicleId = 1;

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.of(vehicle));

        when(vehicle.isAvailable()).thenReturn(true);

        when(rentalRepository.existsActiveRentalForVehicle(vehicleId))
                .thenReturn(false);

        Rental result = rentalService.rentVehicle(
                vehicleId,
                "Masoud",
                startDate,
                endDate
        );

        assertNotNull(result);
        assertEquals(vehicleId, result.getVehicleId());
        assertEquals("Masoud", result.getCustomerName());
        assertEquals(startDate, result.getStartDate());
        assertEquals(endDate, result.getEndDate());
        assertEquals(RentalStatus.ACTIVE, result.getStatus());

        verify(customerService).requireLogin();
        verify(rentalRepository).save(result);
        verify(vehicle).markAsRented();
        verify(vehicleRepository).saveChanges();
    }

    @Test
    void rentVehicle_shouldRejectNullCustomerName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> rentalService.rentVehicle(
                        1,
                        null,
                        startDate,
                        endDate
                )
        );

        verify(customerService).requireLogin();
        verifyNoInteractions(vehicleRepository, rentalRepository);
    }

    @Test
    void rentVehicle_shouldRejectEmptyCustomerName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> rentalService.rentVehicle(
                        1,
                        "   ",
                        startDate,
                        endDate
                )
        );

        verify(customerService).requireLogin();
        verifyNoInteractions(vehicleRepository, rentalRepository);
    }

    @Test
    void rentVehicle_shouldRejectNullStartDate() {
        assertThrows(
                InvalidRentalPeriodException.class,
                () -> rentalService.rentVehicle(
                        1,
                        "Masoud",
                        null,
                        endDate
                )
        );

        verify(customerService).requireLogin();
        verifyNoInteractions(vehicleRepository, rentalRepository);
    }

    @Test
    void rentVehicle_shouldRejectNullEndDate() {
        assertThrows(
                InvalidRentalPeriodException.class,
                () -> rentalService.rentVehicle(
                        1,
                        "Masoud",
                        startDate,
                        null
                )
        );

        verify(customerService).requireLogin();
        verifyNoInteractions(vehicleRepository, rentalRepository);
    }

    @Test
    void rentVehicle_shouldRejectPastStartDate() {
        LocalDate pastDate = LocalDate.now().minusDays(1);

        assertThrows(
                InvalidRentalPeriodException.class,
                () -> rentalService.rentVehicle(
                        1,
                        "Masoud",
                        pastDate,
                        pastDate.plusDays(3)
                )
        );

        verify(customerService).requireLogin();
        verifyNoInteractions(vehicleRepository, rentalRepository);
    }

    @Test
    void rentVehicle_shouldRejectEndDateEqualToStartDate() {
        assertThrows(
                InvalidRentalPeriodException.class,
                () -> rentalService.rentVehicle(
                        1,
                        "Masoud",
                        startDate,
                        startDate
                )
        );

        verify(customerService).requireLogin();
        verifyNoInteractions(vehicleRepository, rentalRepository);
    }

    @Test
    void rentVehicle_shouldRejectEndDateBeforeStartDate() {
        assertThrows(
                InvalidRentalPeriodException.class,
                () -> rentalService.rentVehicle(
                        1,
                        "Masoud",
                        startDate,
                        startDate.minusDays(1)
                )
        );

        verify(customerService).requireLogin();
        verifyNoInteractions(vehicleRepository, rentalRepository);
    }

    @Test
    void rentVehicle_shouldRejectPeriodLongerThanThirtyDays() {
        LocalDate invalidEndDate =
                startDate.plusDays(RentalService.MAX_RENTAL_DAYS + 1);

        assertThrows(
                InvalidRentalPeriodException.class,
                () -> rentalService.rentVehicle(
                        1,
                        "Masoud",
                        startDate,
                        invalidEndDate
                )
        );

        verify(customerService).requireLogin();
        verifyNoInteractions(vehicleRepository, rentalRepository);
    }

    @Test
    void rentVehicle_shouldThrowWhenVehicleDoesNotExist() {
        when(vehicleRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                VehicleNotFoundException.class,
                () -> rentalService.rentVehicle(
                        99,
                        "Masoud",
                        startDate,
                        endDate
                )
        );

        verify(customerService).requireLogin();
        verify(vehicleRepository).findById(99);
        verifyNoInteractions(rentalRepository);
    }

    @Test
    void rentVehicle_shouldThrowWhenVehicleIsNotAvailable() {
        when(vehicleRepository.findById(1))
                .thenReturn(Optional.of(vehicle));

        when(vehicle.isAvailable()).thenReturn(false);

        assertThrows(
                VehicleNotAvailableException.class,
                () -> rentalService.rentVehicle(
                        1,
                        "Masoud",
                        startDate,
                        endDate
                )
        );

        verify(customerService).requireLogin();
        verify(vehicle).isAvailable();
        verify(rentalRepository, never()).save(any());
        verify(vehicle, never()).markAsRented();
    }

    @Test
    void rentVehicle_shouldThrowWhenVehicleHasActiveRental() {
        when(vehicleRepository.findById(1))
                .thenReturn(Optional.of(vehicle));

        when(vehicle.isAvailable()).thenReturn(true);

        when(rentalRepository.existsActiveRentalForVehicle(1))
                .thenReturn(true);

        assertThrows(
                VehicleNotAvailableException.class,
                () -> rentalService.rentVehicle(
                        1,
                        "Masoud",
                        startDate,
                        endDate
                )
        );

        verify(customerService).requireLogin();
        verify(rentalRepository).existsActiveRentalForVehicle(1);
        verify(rentalRepository, never()).save(any());
        verify(vehicle, never()).markAsRented();
    }

    @Test
    void returnVehicle_shouldReturnActiveRentalSuccessfully() {
        Rental rental = new Rental(
                10,
                1,
                "Masoud",
                startDate,
                endDate
        );

        when(rentalRepository.findById(10))
                .thenReturn(Optional.of(rental));

        when(vehicleRepository.findById(1))
                .thenReturn(Optional.of(vehicle));

        Rental result = rentalService.returnVehicle(10);

        assertSame(rental, result);
        assertEquals(RentalStatus.RETURNED, result.getStatus());
        assertFalse(result.isActive());

        verify(customerService).requireLogin();
        verify(vehicle).markAsAvailable();
        verify(vehicleRepository).saveChanges();
    }

    @Test
    void returnVehicle_shouldThrowWhenRentalDoesNotExist() {
        when(rentalRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> rentalService.returnVehicle(99)
        );

        verify(customerService).requireLogin();
        verify(vehicleRepository, never()).findById(anyInt());
        verify(vehicleRepository, never()).saveChanges();
    }

    @Test
    void returnVehicle_shouldThrowWhenRentalIsNotActive() {
        Rental rental = new Rental(
                10,
                1,
                "Masoud",
                startDate,
                endDate
        );

        rental.setStatus(RentalStatus.RETURNED);

        when(rentalRepository.findById(10))
                .thenReturn(Optional.of(rental));

        assertThrows(
                IllegalStateException.class,
                () -> rentalService.returnVehicle(10)
        );

        verify(customerService).requireLogin();
        verify(vehicleRepository, never()).findById(anyInt());
        verify(vehicleRepository, never()).saveChanges();
    }

    @Test
    void returnVehicle_shouldThrowWhenVehicleDoesNotExist() {
        Rental rental = new Rental(
                10,
                1,
                "Masoud",
                startDate,
                endDate
        );

        when(rentalRepository.findById(10))
                .thenReturn(Optional.of(rental));

        when(vehicleRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                VehicleNotFoundException.class,
                () -> rentalService.returnVehicle(10)
        );

        assertEquals(RentalStatus.ACTIVE,rental.getStatus());

        verify(customerService).requireLogin();
        verify(vehicleRepository, never()).saveChanges();
    }

    @Test
    void calculateRentalCost_shouldCalculateCorrectCost() {

    Rental rental = new Rental(
            10,
            1,
            "Masoud",
            startDate,
            endDate
    );

    when(rentalRepository.findById(10))
            .thenReturn(Optional.of(rental));

    when(vehicleRepository.findById(1))
            .thenReturn(Optional.of(vehicle));

    when(vehicle.calculateRentalCost(4)).thenReturn(160.0);

    double result = rentalService.calculateRentalCost(10);

    assertEquals(160.0,result,0.001);

    verify(customerService).requireLogin();
    verify(vehicle).calculateRentalCost(4);
}

    @Test
    void calculateRentalCost_shouldThrowWhenRentalDoesNotExist() {
        when(rentalRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> rentalService.calculateRentalCost(99)
        );

        verify(customerService).requireLogin();
        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void calculateRentalCost_shouldThrowWhenVehicleDoesNotExist() {
        Rental rental = new Rental(
                10,
                1,
                "Masoud",
                startDate,
                endDate
        );

        when(rentalRepository.findById(10))
                .thenReturn(Optional.of(rental));

        when(vehicleRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                VehicleNotFoundException.class,
                () -> rentalService.calculateRentalCost(10)
        );

        verify(customerService).requireLogin();
    }

    @Test
    void getAllRentals_shouldReturnRepositoryRentals() {
        Rental firstRental = new Rental(
                1,
                1,
                "Masoud",
                startDate,
                endDate
        );

        Rental secondRental = new Rental(
                2,
                2,
                "Ahmad",
                startDate,
                endDate
        );

        List<Rental> rentals = List.of(firstRental, secondRental);

        when(rentalRepository.getAllRentals())
                .thenReturn(rentals);

        List<Rental> result = rentalService.getAllRentals();

        assertSame(rentals, result);
        assertEquals(2, result.size());

        verify(rentalRepository).getAllRentals();
    }
}