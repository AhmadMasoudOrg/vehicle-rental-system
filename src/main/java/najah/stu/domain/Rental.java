package najah.stu.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a vehicle rental.
 *
 * A rental contains the customer name, vehicle ID,
 * rental dates, return date and current rental status.
 */
public class Rental {

    private int id;
    private int vehicleId;
    private String customerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate returnDate;
    private RentalStatus status;

    /**
     * Creates a new active rental without a return date.
     *
     * @param id rental ID
     * @param vehicleId rented vehicle ID
     * @param customerName customer name
     * @param startDate rental start date
     * @param endDate rental end date
     */
    public Rental(int id, int vehicleId, String customerName, LocalDate startDate, LocalDate endDate) {

        this.id = id;
        this.vehicleId = vehicleId;
        this.customerName = customerName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.returnDate = null;
        this.status = RentalStatus.ACTIVE;
    }

    /**
     * Creates a rental with a specified status.
     *
     * @param id rental ID
     * @param vehicleId rented vehicle ID
     * @param customerName customer name
     * @param startDate rental start date
     * @param endDate rental end date
     * @param status rental status
     */
    public Rental(int id, int vehicleId, String customerName, LocalDate startDate, LocalDate endDate, RentalStatus status) {

        this.id = id;
        this.vehicleId = vehicleId;
        this.customerName = customerName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.returnDate = null;
        this.status = status;
    }

    /**
     * Creates a rental with a return date and status.
     *
     * @param id rental ID
     * @param vehicleId rented vehicle ID
     * @param customerName customer name
     * @param startDate rental start date
     * @param endDate rental end date
     * @param returnDate actual vehicle return date
     * @param status rental status
     */
    public Rental(int id, int vehicleId, String customerName, LocalDate startDate, LocalDate endDate, LocalDate returnDate, RentalStatus status) {

        this.id = id;
        this.vehicleId = vehicleId;
        this.customerName = customerName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    /**
     * Returns the rental ID.
     *
     * @return rental ID
     */
    public int getId() {

        return id;
    }

    /**
     * Updates the rental ID.
     *
     * @param id rental ID
     */
    public void setId(int id) {

        this.id = id;
    }

    /**
     * Returns the rented vehicle ID.
     *
     * @return vehicle ID
     */
    public int getVehicleId() {

        return vehicleId;
    }

    /**
     * Updates the rented vehicle ID.
     *
     * @param vehicleId vehicle ID
     */
    public void setVehicleId(int vehicleId) {

        this.vehicleId = vehicleId;
    }

    /**
     * Returns the customer name.
     *
     * @return customer name
     */
    public String getCustomerName() {

        return customerName;
    }

    /**
     * Updates the customer name.
     *
     * @param customerName customer name
     */
    public void setCustomerName(String customerName) {

        this.customerName = customerName;
    }

    /**
     * Returns the rental start date.
     *
     * @return rental start date
     */
    public LocalDate getStartDate() {

        return startDate;
    }

    /**
     * Updates the rental start date.
     *
     * @param startDate rental start date
     */
    public void setStartDate(LocalDate startDate) {

        this.startDate = startDate;
    }

    /**
     * Returns the rental end date.
     *
     * @return rental end date
     */
    public LocalDate getEndDate() {

        return endDate;
    }

    /**
     * Updates the rental end date.
     *
     * @param endDate rental end date
     */
    public void setEndDate(LocalDate endDate) {

        this.endDate = endDate;
    }

    /**
     * Returns the actual vehicle return date.
     *
     * @return return date or null when the vehicle was not returned
     */
    public LocalDate getReturnDate() {

        return returnDate;
    }

    /**
     * Updates the actual vehicle return date.
     *
     * @param returnDate actual return date
     */
    public void setReturnDate(LocalDate returnDate) {

        this.returnDate = returnDate;
    }

    /**
     * Returns the rental status.
     *
     * @return rental status
     */
    public RentalStatus getStatus() {

        return status;
    }

    /**
     * Updates the rental status.
     *
     * @param status rental status
     */
    public void setStatus(RentalStatus status) {

        this.status = status;
    }

    /**
     * Checks whether the rental is active.
     *
     * @return true when the rental status is active
     */
    public boolean isActive() {

        return status == RentalStatus.ACTIVE;
    }

    /**
     * Calculates the planned rental duration.
     *
     * @return number of days between the start date and end date
     */
    public long getDurationInDays() {

        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * Calculates the number of late return days.
     *
     * @return number of late days or zero when the vehicle
     *         was returned on time or was not returned yet
     */
    public long getLateDays() {

        if (returnDate == null) {

            return 0;
        }

        if (!returnDate.isAfter(endDate)) {

            return 0;
        }

        return ChronoUnit.DAYS.between(endDate, returnDate);
    }
}