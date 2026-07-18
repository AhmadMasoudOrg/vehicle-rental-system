package najah.stu.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import najah.stu.domain.Rental;
import najah.stu.domain.RentalStatus;
import najah.stu.domain.Vehicle;
import najah.stu.exception.InvalidRentalPeriodException;
import najah.stu.exception.VehicleNotAvailableException;
import najah.stu.exception.VehicleNotFoundException;
import najah.stu.repository.RentalRepository;
import najah.stu.repository.VehicleRepository;

/**
 * Provides the main operations related to vehicle rentals.
 *
 * This service allows customers to rent vehicles,
 * return vehicles and calculate rental costs.
 */
public class RentalService {

    public static final long MIN_RENTAL_DAYS = 1;
    public static final long MAX_RENTAL_DAYS = 30;
    public static final double LATE_PENALTY_PER_DAY = 10.0;

    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;
    private final CustomerService customerService;

    /**
     * Creates a rental service using the default repositories
     * and customer service.
     */
    public RentalService() {

        this(
                new VehicleRepository(),
                new RentalRepository(),
                new CustomerService()
        );
    }

    /**
     * Creates a rental service using the provided dependencies.
     *
     * @param vehicleRepository repository used to access vehicles
     * @param rentalRepository repository used to access rentals
     * @param customerService service used to verify customer login
     */
    public RentalService(VehicleRepository vehicleRepository,
                         RentalRepository rentalRepository,
                         CustomerService customerService) {

        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
        this.customerService = customerService;
    }

    /**
     * Rents an available vehicle for the logged-in customer.
     *
     * @param vehicleId ID of the vehicle
     * @param customerName name of the customer
     * @param startDate rental start date
     * @param endDate rental end date
     * @return the created rental
     * @throws IllegalArgumentException if the customer name is empty
     * @throws InvalidRentalPeriodException if the rental dates are invalid
     * @throws VehicleNotFoundException if the vehicle does not exist
     * @throws VehicleNotAvailableException if the vehicle is already rented
     */
    public Rental rentVehicle(int vehicleId,
                              String customerName,
                              LocalDate startDate,
                              LocalDate endDate) {

        customerService.requireLogin();

        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Customer name must not be empty."
            );
        }

        validateRentalPeriod(startDate,endDate);

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException(
                        "Vehicle not found with id: " + vehicleId
                ));

        if (!vehicle.isAvailable()
                || rentalRepository.existsActiveRentalForVehicle(vehicleId)) {

            throw new VehicleNotAvailableException(
                    "Vehicle with id "
                            + vehicleId
                            + " is already rented."
            );
        }

        Rental rental = new Rental(
                0,
                vehicleId,
                customerName,
                startDate,
                endDate
        );

        rentalRepository.save(rental);

        vehicle.markAsRented();

        vehicleRepository.saveChanges();

        return rental;
    }

    /**
     * Returns a vehicle from an active rental.
     *
     * The rental status is changed to returned and the vehicle
     * becomes available again.
     *
     * @param rentalId ID of the rental
     * @return the returned rental
     * @throws IllegalArgumentException if the rental does not exist
     * @throws IllegalStateException if the rental is not active
     * @throws VehicleNotFoundException if the rented vehicle does not exist
     */
    public Rental returnVehicle(int rentalId) {

        customerService.requireLogin();

        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Rental not found with id: " + rentalId
                ));

        if (!rental.isActive()) {
            throw new IllegalStateException(
                    "Rental with id "
                            + rentalId
                            + " is not active."
            );
        }

        Vehicle vehicle = vehicleRepository.findById(rental.getVehicleId())
                .orElseThrow(() -> new VehicleNotFoundException(
                        "Vehicle not found with id: "
                                + rental.getVehicleId()
                ));

        rental.setReturnDate(LocalDate.now());

        rental.setStatus(RentalStatus.RETURNED);

        vehicle.markAsAvailable();

        rentalRepository.saveChanges();

        vehicleRepository.saveChanges();

        return rental;
    }

    /**
     * Returns all rentals stored in the rental repository.
     *
     * @return list of all rentals
     */
    public List<Rental> getAllRentals() {

        return rentalRepository.getAllRentals();
    }

    /**
     * Validates the start and end dates of a rental.
     *
     * @param startDate rental start date
     * @param endDate rental end date
     * @throws InvalidRentalPeriodException if the dates are invalid
     */
    private void validateRentalPeriod(LocalDate startDate,
                                      LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new InvalidRentalPeriodException(
                    "Start date and end date are required."
            );
        }

        if (startDate.isBefore(LocalDate.now())) {
            throw new InvalidRentalPeriodException(
                    "Start date cannot be in the past."
            );
        }

        if (!endDate.isAfter(startDate)) {
            throw new InvalidRentalPeriodException(
                    "End date must be after the start date."
            );
        }

        long durationInDays = ChronoUnit.DAYS.between(
                startDate,
                endDate
        );

        if (durationInDays < MIN_RENTAL_DAYS
                || durationInDays > MAX_RENTAL_DAYS) {

            throw new InvalidRentalPeriodException(
                    "Rental duration must be between "
                            + MIN_RENTAL_DAYS
                            + " and "
                            + MAX_RENTAL_DAYS
                            + " days."
            );
        }
    }

    /**
     * Calculates the total cost of a rental.
     *
     * The calculation uses the vehicle pricing strategy.
     * A late penalty is added when the vehicle is returned
     * after the rental end date.
     *
     * @param rentalId ID of the rental
     * @return total rental cost
     * @throws IllegalArgumentException if the rental does not exist
     * @throws VehicleNotFoundException if the vehicle does not exist
     */
    public double calculateRentalCost(int rentalId) {

        customerService.requireLogin();

        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Rental not found with id: " + rentalId
                ));

        Vehicle vehicle = vehicleRepository.findById(rental.getVehicleId())
                .orElseThrow(() -> new VehicleNotFoundException(
                        "Vehicle not found with id: "
                                + rental.getVehicleId()
                ));

        double totalCost = vehicle.calculateRentalCost(
                rental.getDurationInDays()
        );

        long lateDays = rental.getLateDays();

        if (lateDays > 0) {
            totalCost = totalCost
                    + (lateDays * LATE_PENALTY_PER_DAY);
        }

        return totalCost;
    }
}