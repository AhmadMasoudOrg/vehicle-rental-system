package najah.stu.domain;

import najah.stu.strategy.PricingStrategy;

/**
 * Represents a vehicle in the rental system.
 *
 * A vehicle contains identification information,
 * availability status, daily rate and a pricing strategy.
 */
public class Vehicle {

    private int id;
    private String brand;
    private String model;
    private VehicleStatus status;
    private double dailyRate;
    private PricingStrategy pricingStrategy;

    /**
     * Creates a vehicle with a pricing strategy.
     *
     * @param id vehicle ID
     * @param brand vehicle brand
     * @param model vehicle model
     * @param available vehicle availability
     * @param dailyRate vehicle daily rental rate
     * @param pricingStrategy strategy used to calculate rental cost
     */
    public Vehicle(int id,String brand,String model,boolean available,double dailyRate,PricingStrategy pricingStrategy) {

        this.id = id;
        this.brand = brand;
        this.model = model;
        this.status = available ? VehicleStatus.AVAILABLE : VehicleStatus.RENTED;
        this.pricingStrategy = pricingStrategy;

        setDailyRate(dailyRate);
    }

    /**
     * Creates a vehicle without a specific pricing strategy.
     *
     * The normal daily rate calculation is used.
     *
     * @param id vehicle ID
     * @param brand vehicle brand
     * @param model vehicle model
     * @param available vehicle availability
     * @param dailyRate vehicle daily rental rate
     */
    public Vehicle(int id,String brand,String model,boolean available,double dailyRate) {

        this(
                id,
                brand,
                model,
                available,
                dailyRate,
                null
        );
    }

    /**
     * Returns the vehicle ID.
     *
     * @return vehicle ID
     */
    public int getId() {

        return id;
    }

    /**
     * Updates the vehicle ID.
     *
     * @param id vehicle ID
     */
    public void setId(int id) {

        this.id = id;
    }

    /**
     * Returns the vehicle brand.
     *
     * @return vehicle brand
     */
    public String getBrand() {

        return brand;
    }

    /**
     * Updates the vehicle brand.
     *
     * @param brand vehicle brand
     */
    public void setBrand(String brand) {

        this.brand = brand;
    }

    /**
     * Returns the vehicle model.
     *
     * @return vehicle model
     */
    public String getModel() {

        return model;
    }

    /**
     * Updates the vehicle model.
     *
     * @param model vehicle model
     */
    public void setModel(String model) {

        this.model = model;
    }

    /**
     * Returns the vehicle status.
     *
     * @return vehicle status
     */
    public VehicleStatus getStatus() {

        return status;
    }

    /**
     * Updates the vehicle status.
     *
     * @param status vehicle status
     */
    public void setStatus(VehicleStatus status) {

        this.status = status;
    }

    /**
     * Checks whether the vehicle is available.
     *
     * @return true when the vehicle is available
     */
    public boolean isAvailable() {

        return status == VehicleStatus.AVAILABLE;
    }

    /**
     * Returns the daily rental rate.
     *
     * @return daily rental rate
     */
    public double getDailyRate() {

        return dailyRate;
    }

    /**
     * Updates the daily rental rate.
     *
     * @param dailyRate daily rental rate
     * @throws IllegalArgumentException if the rate is negative
     */
    public void setDailyRate(double dailyRate) {

        if (dailyRate < 0) {
            throw new IllegalArgumentException("Daily rate cannot be negative.");
        }

        this.dailyRate = dailyRate;
    }

    /**
     * Returns the vehicle type.
     *
     * The base vehicle type is car.
     *
     * @return vehicle type
     */
    public VehicleType getType() {

        return VehicleType.CAR;
    }

    /**
     * Calculates the rental cost.
     *
     * The pricing strategy is used when available.
     * Otherwise, the normal daily rate calculation is used.
     *
     * @param rentalDays number of rental days
     * @return total rental cost
     */
    public double calculateRentalCost(long rentalDays) {

        if (pricingStrategy == null) {
            return rentalDays * dailyRate;
        }

        return pricingStrategy.calculateCost(rentalDays,dailyRate);
    }

    /**
     * Updates the vehicle availability.
     *
     * @param available true to make the vehicle available
     */
    public void setAvailable(boolean available) {

        this.status = available ? VehicleStatus.AVAILABLE : VehicleStatus.RENTED;
    }

    /**
     * Marks the vehicle as rented.
     */
    public void markAsRented() {

        this.status = VehicleStatus.RENTED;
    }

    /**
     * Marks the vehicle as available.
     */
    public void markAsAvailable() {

        this.status = VehicleStatus.AVAILABLE;
    }

    /**
     * Returns the current pricing strategy.
     *
     * @return pricing strategy
     */
    public PricingStrategy getPricingStrategy() {

        return pricingStrategy;
    }

    /**
     * Updates the pricing strategy.
     *
     * @param pricingStrategy pricing strategy
     */
    public void setPricingStrategy(PricingStrategy pricingStrategy) {

        this.pricingStrategy = pricingStrategy;
    }
}