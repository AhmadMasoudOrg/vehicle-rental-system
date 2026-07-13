package najah.stu.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;




import najah.stu.domain.Rental;
import najah.stu.domain.Vehicle;
import najah.stu.exception.InvalidRentalPeriodException;
import najah.stu.exception.VehicleNotAvailableException;
import najah.stu.exception.VehicleNotFoundException;
import najah.stu.repository.RentalRepository;
import najah.stu.repository.VehicleRepository;

public class RentalService {
    public static final long MIN_RENTAL_DAYS = 1;
    public static final long MAX_RENTAL_DAYS = 30;

    
    
    
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;
    private final CustomerService customerService;

    public RentalService() {
        this(new VehicleRepository(), new RentalRepository(), new CustomerService());
    }

    public RentalService(VehicleRepository vehicleRepository,
                          RentalRepository rentalRepository,
                          CustomerService customerService) {
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
        this.customerService = customerService;
    }

    
    
    
    
    public Rental rentVehicle(int vehicleId, String customerName, LocalDate startDate, LocalDate endDate) {

        customerService.requireLogin();

        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name must not be empty.");
        }

        validateRentalPeriod(startDate, endDate);

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with id: " + vehicleId));

        if (!vehicle.isAvailable() || rentalRepository.existsActiveRentalForVehicle(vehicleId)) {
            throw new VehicleNotAvailableException(
                    "Vehicle with id " + vehicleId + " is already rented.");
        }

        Rental rental = new Rental(0, vehicleId, customerName, startDate, endDate);
        rentalRepository.save(rental);

        vehicle.markAsRented();
        vehicleRepository.saveChanges();

        return rental;
    }

    public Rental returnVehicle(int rentalId) {

        customerService.requireLogin();

        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Rental not found with id: " + rentalId));

        if (!rental.isActive()) {
            throw new IllegalStateException("Rental with id " + rentalId + " is not active.");
        }

        rental.setStatus(najah.stu.domain.RentalStatus.RETURNED);

        Vehicle vehicle = vehicleRepository
        .findById(rental.getVehicleId())
        .orElseThrow(() ->
                new VehicleNotFoundException(
                        "Vehicle not found with id: "
                                + rental.getVehicleId()
                )
        );

vehicle.markAsAvailable();
vehicleRepository.saveChanges();

        return rental;
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.getAllRentals();
    }

    private void validateRentalPeriod(LocalDate startDate, LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new InvalidRentalPeriodException("Start date and end date are required.");
        }

        if (startDate.isBefore(LocalDate.now())) {
            throw new InvalidRentalPeriodException("Start date cannot be in the past.");
        }

        if (!endDate.isAfter(startDate)) {
            throw new InvalidRentalPeriodException("End date must be after the start date.");
        }

        long durationInDays = ChronoUnit.DAYS.between(startDate, endDate);

        if (durationInDays < MIN_RENTAL_DAYS || durationInDays > MAX_RENTAL_DAYS) {
            throw new InvalidRentalPeriodException(
                    "Rental duration must be between " + MIN_RENTAL_DAYS
                            + " and " + MAX_RENTAL_DAYS + " days.");
        }
    }
}