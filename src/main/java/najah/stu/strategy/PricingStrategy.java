package najah.stu.strategy;

/**
 * Defines the pricing calculation used by vehicles.
 *
 * Different vehicle types can provide different
 * implementations of this strategy.
 */
public interface PricingStrategy {

    /**
     * Calculates the rental cost.
     *
     * @param rentalDays number of rental days
     * @param dailyRate vehicle daily rental rate
     * @return total rental cost
     */
    double calculateCost(long rentalDays,double dailyRate);
}