package najah.stu.strategy;

public class SuvPricingStrategy implements PricingStrategy {

    private static final double SUV_RATE_MULTIPLIER = 1.10;

    @Override
    public double calculateCost(long rentalDays,double dailyRate) {

        double normalCost = rentalDays * dailyRate;

        return normalCost * SUV_RATE_MULTIPLIER;
    }
}