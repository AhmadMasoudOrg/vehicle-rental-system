package najah.stu.strategy;

public class TruckPricingStrategy implements PricingStrategy {

    private static final double TRUCK_RATE_MULTIPLIER = 1.20;

    @Override
    public double calculateCost(long rentalDays,double dailyRate) {

        double normalCost = rentalDays * dailyRate;

        return normalCost * TRUCK_RATE_MULTIPLIER;
    }
}